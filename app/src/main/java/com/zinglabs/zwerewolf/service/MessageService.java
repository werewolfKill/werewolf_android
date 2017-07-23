package com.zinglabs.zwerewolf.service;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;

import com.zinglabs.zwerewolf.constant.ProtocolConstant;
import com.zinglabs.zwerewolf.data.GameChatData;
import com.zinglabs.zwerewolf.entity.Packet;
import com.zinglabs.zwerewolf.entity.User;

import java.nio.charset.Charset;
import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import static com.zinglabs.zwerewolf.config.Constants.CHANNEL;
import static com.zinglabs.zwerewolf.config.Constants.ENCODING;
import static com.zinglabs.zwerewolf.config.Constants.FREQUENCY;

/**
 * author: vector.huang
 * date：2016/4/18 22:06
 */
public class MessageService {
    private AudioRecord audioRecord;
    private  AudioTrack audioTrack;
    private boolean isRecording;

    public void sendText(Channel channel,int from, String content){
        byte[] bytes = content.getBytes();
        ByteBuf buf = channel.alloc().buffer(bytes.length+8);
        buf.writeInt(from);
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
        // 12 = 1个int + 4个short
        Packet packet = new Packet(buf.readableBytes() + 12,
                ProtocolConstant.SID_MSG,ProtocolConstant.CID_MSG_TEXT_REQ
                ,buf);
        channel.writeAndFlush(packet);
    }

    public GameChatData receiveText(ByteBuf body){
        int userId = body.readInt();
        String userName = body.readBytes(body.readInt()).toString(Charset.defaultCharset());
        String msg = body.readBytes(body.readInt()).toString(Charset.defaultCharset());
        GameChatData gameChatData = new GameChatData(GameChatData.CHAT, new Date().getTime() + "", new User(userId,userName), 111, msg);
        return gameChatData;
    }

    public void sendVoiceStart(Channel channel,int from){ // 开始发言
        int bufferSize = AudioRecord.getMinBufferSize(FREQUENCY, CHANNEL, ENCODING);
        if(!audioRecordOpen(bufferSize)){
            return ;
        }
        byte[] audiodata = new byte[bufferSize];
        audioRecord.startRecording();
        isRecording = true;
        new Thread(() ->{
            while (isRecording) {
                audioRecord.read(audiodata, 0, bufferSize);
                ByteBuf buf = channel.alloc().buffer(audiodata.length + 8);
                buf.writeInt(from);
                buf.writeInt(audiodata.length);
                buf.writeBytes(audiodata);
                Packet packet = new Packet(buf.readableBytes() + 12,
                        ProtocolConstant.SID_MSG,ProtocolConstant.CID_MSG_VOICE_REQ
                        ,buf);
                channel.writeAndFlush(packet);
                //isRecording = false;
            }
            audioRecord.stop();
        }).start();
    }
    public void sendVoiceInterrupt(Channel channel){ // 中断发言
        isRecording = false;
    }
    public void receiveVoice(ByteBuf body){ // 接收语音
        if(audioTrack == null){
            audioTrackOpen();
        }
        int audiodataLength =  body.readInt();
        byte[] audiodata = new byte[audiodataLength];
        body.readBytes(audiodataLength).readBytes(audiodata);
        audioTrack.write(audiodata,0,audiodataLength);
    }

    private boolean audioRecordOpen(int bufferSize) { // 打开手机录音设备
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.DEFAULT,
                FREQUENCY, CHANNEL, ENCODING, bufferSize);
        if (audioRecord.getState() == AudioRecord.STATE_INITIALIZED) {
            audioRecord.startRecording();
            return true;
        }
        return false;
    }

    private void audioTrackOpen(){ // 打开播放设备
        audioTrack = new AudioTrack(
                AudioManager.STREAM_MUSIC,
                FREQUENCY,
                AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT,
                AudioTrack.getMinBufferSize (
                        FREQUENCY,
                        AudioFormat.CHANNEL_OUT_STEREO,
                        AudioFormat.ENCODING_PCM_16BIT
                ),
                AudioTrack.MODE_STREAM
        );
        audioTrack.play();
    }

}
