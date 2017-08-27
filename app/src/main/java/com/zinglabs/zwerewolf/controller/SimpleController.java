package com.zinglabs.zwerewolf.controller;

import android.app.Activity;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;

import com.zinglabs.zwerewolf.config.Constants;
import com.zinglabs.zwerewolf.constant.ProtocolConstant;
import com.zinglabs.zwerewolf.entity.Room;
import com.zinglabs.zwerewolf.event.GameStateMessage;
import com.zinglabs.zwerewolf.manager.DialogManager;
import com.zinglabs.zwerewolf.role.Prophet;
import com.zinglabs.zwerewolf.role.Role;
import com.zinglabs.zwerewolf.role.Witch;
import com.zinglabs.zwerewolf.role.Wolf;
import com.zinglabs.zwerewolf.utils.IMClientUtil;
import com.zinglabs.zwerewolf.utils.LogUtil;
import com.zinglabs.zwerewolf.utils.RoleUtil;
import com.zinglabs.zwerewolf.utils.StringUtils;
import com.zinglabs.zwerewolf.widget.RoleView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 简单模式游戏流程控制器
 * Created by Administrator on 2017/3/13.
 */

public class SimpleController implements Role.OnRoleStateChangeListener {
    private static final String STAGE_DARK = "天黑";
    private static final String STAGE_PROPHET = "预言家";
    private static final String STAGE_WOLF = "狼人";
    private static final String STAGE_WITCH = "女巫";
    private static final String STAGE_HUNTSMAN = "猎人";
    private static final String STAGE_GUARD = "守卫";
    private static final String STAGE_VILLAGER = "村民";
    private static final String STAGE_IDIOT = "白痴";
    private static final String STAGE_DAWN = "天亮";
    public static final String STAGE_TALK = "讨论";
    public static final String STAGE_VOTE = "投票";
    private static final String STAGE_TOUPIAOJIEGUO = "投票结果";

    private int winer = Role.WIN_NO;//阵营不要放在role里
    private static boolean isGameing;

    private String curStage;
    private Map<String, Integer> elementMap;//角色组成情况，key=角色名，value=人数
    private Map<Integer, Role> deployMap;//角色部署情况，key=玩家编号，value=角色名

    private List<Integer> aliveList;//幸存者集合

    private Handler handler;

    /**
     * 流程控制器一开始就拥有handler通知界面变化
     */
    public SimpleController(Handler handler) {
        this.handler = handler;
    }

    public static boolean isGameing() {
        return isGameing;
    }

    public boolean isAlive(int number) {
        return deployMap.get(number).isAlive();
    }

    public String getCurStage() {
        return curStage;
    }

    public Role getRole(Integer number) {
        if (deployMap.containsKey(number)) {
            return deployMap.get(number);
        }
        return null;
    }


    public void readyGame(Room room) {
        Map<String, Object> param = new HashMap<>();
        int userId = room.getCurUserId();
        int roomId = room.getRoomId();
        param.put("fromId", userId);
        param.put("roomId", roomId);
        param.put("content", 0);  //设为0
        param.put("bout", 0);  //设为0

        //向服务器发送准备游戏通知
        IMClientUtil.sendMsg(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_READY_REQ, param);

    }

    public void startGame(Room room) {
        Map<String, Object> param = new HashMap<>();
        int userId = room.getCurUserId();
        int roomId = room.getRoomId();
        param.put("fromId", userId);
        param.put("roomId", roomId);
        param.put("content", 0);  //设为0
        param.put("bout", 0);  //设为0

        //向服务器发送开始游戏通知
        IMClientUtil.sendMsg(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_START_REQ, param);


    }

    public void setAtGame(Map<Integer, RoleView> roleViewMap) {
        for (RoleView roleView : roleViewMap.values()) {
            roleView.setAtGame();
        }
    }


    public void doDark(Activity activity, Room room) {  //天黑流程
        int userId = room.getCurUserId();
        int roomId = room.getRoomId();
        int curPos = room.getPlayers().get(userId).getPosition();
        Role role = room.getPlayers().get(userId).getRole();
        int modalId = room.getModelId();
        int roleId = room.getPlayers().get(userId).getRole().getId();
        int bout = room.getBout();
        Runnable runnable = null;
        long waitTime;
        switch (role.getName()) {
            case STAGE_WOLF:     //狼人
                Map<String, Integer> killMap = new HashMap<>();
                killMap.put("fromId", userId);
                killMap.put("roomId", roomId);
                killMap.put("bout", bout);
                String title = "您要击杀的人是：";
                waitTime = RoleUtil.getWaitTime(roleId, modalId);
                DialogManager.showModalChoice(activity, title, StringUtils.trans2StrArr(room.getLiveList(),""), ProtocolConstant.CID_GAME_KILL_REQ, killMap);

                break;
            case STAGE_PROPHET:  //预言家
                Map<String, Integer> testMap = new HashMap<>();
                testMap.put("fromId", userId);
                testMap.put("content", userId);
                testMap.put("roomId", roomId);
                testMap.put("bout", bout);
                title = "您要验的人是：";
                waitTime = RoleUtil.getWaitTime(roleId, modalId);
                DialogManager.showModalChoice(activity, title, StringUtils.trans2StrArr(room.getLiveList(),""), ProtocolConstant.CID_GAME_VERIFY_REQ, testMap);
                break;
            case STAGE_GUARD:    //守卫
                Map<String, Integer> guardMap = new HashMap<>();
                guardMap.put("fromId", userId);
                guardMap.put("content", userId);
                guardMap.put("roomId", roomId);
                guardMap.put("bout", bout);
                title = "您要守卫的人是：";
                waitTime = RoleUtil.getWaitTime(roleId, modalId);
                DialogManager.showModalChoice(activity, title, StringUtils.trans2StrArr(room.getLiveList(),""), ProtocolConstant.CID_GAME_GUARD_REQ, guardMap);
                break;
            case STAGE_WITCH:   //女巫
                waitTime = RoleUtil.getWaitTime(roleId, modalId);
                DialogManager.showWaitOtherDialog(activity, (int) waitTime);

                Map<String, Integer> witchMap = new HashMap<>();
                witchMap.put("fromId", userId);
                witchMap.put("roomId", roomId);
                witchMap.put("bout", bout);

                Witch witch = (Witch) role;
                if (!witch.hasPanacea() && witch.hasPoison()) { //没有解药但有毒药
                    DialogManager.showWitchPoisonDialog(activity, witchMap, (int) waitTime, witch,room.getLiveList());
                }
                break;

            default:  //村民、猎人
                waitTime = RoleUtil.getWaitTime(roleId, modalId);
                DialogManager.showWaitOtherDialog(activity, (int) waitTime);
        }

    }

    /**
     * 女巫操作
     *
     * @param activity
     * @param room
     * @param reply
     */
    public void witchAction(Activity activity, Room room, int reply) {
        int userId = room.getCurUserId();
        int roomId = room.getRoomId();
        int bout = room.getBout();
        Witch witch = (Witch) room.getPlayers().get(userId).getRole();
        int roleId = witch.getId();
        int modalId = room.getModelId();
        long waitTime = RoleUtil.getWaitTime(roleId, modalId);
        Map<String, Integer> witchMap = new HashMap<>();
        witchMap.put("fromId", userId);
        witchMap.put("roomId", roomId);
        witchMap.put("bout", bout);
        waitTime = RoleUtil.getWaitTime(roleId, modalId);
        DialogManager.showWitchSaveDialog(activity, reply, witchMap, (int) waitTime, witch,room.getLiveList());

    }

    /**
     * 放弃竞选（退水）
     * @param activity
     * @param room
     */
    public void cancelVoteChief(Activity activity, Room room){
        int userId = room.getCurUserId();
        int roomId = room.getRoomId();
        int bout = room.getBout();
        Map<String, Integer> param = new HashMap<>();
        param.put("fromId", userId);
        param.put("roomId", roomId);
        param.put("bout", bout);
        String title = "确定放弃竞选吗？";
        DialogManager.showCommonDialog(activity,ProtocolConstant.CID_GAME_QUIT_POLICE,title,param,"是","否");
    }

    /**
     * 天亮流程
     *
     * @param activity activity
     * @param room     房间信息
     */
    public void doDawn(Activity activity, Room room, Integer[] kills, Map<Integer, RoleView> roleViewMap, short cid) {
        int bout = room.getBout();
        List<Integer> deads = new ArrayList<>();
        if (kills != null) {
            List<Integer> list = Arrays.asList(kills);
            if (!(list.size() == 1 && list.get(0) == 0)) {
                room.addDeadList(bout, list);
                deads = Arrays.asList(kills);
            }
        }
        if (room.isOver()) {
            DialogManager.showOverDialog(activity, kills,room.getOver());
            return;
        }
        int userId = room.getCurUserId();
        int roomId = room.getRoomId();
        if (bout == 1) {  //第1天，警长竞选
            String title = "您是否要竞选警长？";
            Map<String, Integer> param = new HashMap<>();
            param.put("fromId", userId);
            param.put("roomId", roomId);
            param.put("bout", bout);
            DialogManager.showCommonDialog(activity, ProtocolConstant.CID_GAME_ASK_CHIEF, title, param, "是", "不了");
        } else {  //
            int chief = room.getChief();
            if (chief > 0) {  //有警长
                if (room.getCurUserPos() == chief) {
                    this.turnSpeakByChief(activity, room, deads);
                }
            } else {
                this.turnSpeak(roleViewMap, room.getLiveList(), room, cid);
            }
        }
    }

    /**
     * 流警徽
     */
    public void changeChief(Activity activity,Room room){

        Map<String, Integer> param = new HashMap<>();
        int userId = room.getCurUserId();
        int roomId = room.getRoomId();
        int bout = room.getBout();
        param.put("fromId", userId);
        param.put("roomId", roomId);
        param.put("bout", bout);  //设为0
        String title = "请选择将警徽交给...";

        DialogManager.showModalChoice2(activity, title, "撕掉警徽",StringUtils.trans2StrArr(room.getLiveList(), ""), ProtocolConstant.CID_GAME_CHANGE_CHIEF, param);

    }

    /**
     * 轮流发言
     *
     * @param roleViewMap
     * @param speakers
     * @param room
     */
    public void turnSpeak(Map<Integer, RoleView> roleViewMap, List<Integer> speakers, Room room, short cid) {

        int size = speakers.size();
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(size);
        queue.addAll(speakers);

        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(() -> {
            Integer turnPos = queue.poll();
            if (turnPos == null) {
                for (Map.Entry<Integer, RoleView> entry : roleViewMap.entrySet()) {
                    entry.getValue().unSpeak();
                }
                Map<String, Object> param = new HashMap<>();
                param.put("fromId", room.getCurUserId());
                param.put("roomId", room.getRoomId());
                param.put("bout", room.getBout());
                param.put("content", 0);
                IMClientUtil.sendMsg(ProtocolConstant.SID_GAME, cid, param);
                service.shutdown();
                return;
            }
            //TODO 遍历每个角色，待优化
            for (Map.Entry<Integer, RoleView> entry : roleViewMap.entrySet()) {
                if (entry.getKey().equals(turnPos)) {
                    entry.getValue().speak();
                } else {
                    entry.getValue().unSpeak();
                }
            }
        }, 0, Constants.VOTE_CHIEF_SPEAK_TIME, TimeUnit.MILLISECONDS);
    }

    public void turnSpeakByChief(Activity activity, Room room, List<Integer> deadList) {

        String title = "请选择从警左或警右开始发言";
        if (deadList != null && deadList.size() > 0) {
            title = "请选择从死左或死右开始发言";
        }
        Map<String, Integer> param = new HashMap<>();
        param.put("fromId", room.getCurUserId());
        param.put("roomId", room.getRoomId());
        param.put("bout", room.getBout());
        DialogManager.showCommonDialog(activity, ProtocolConstant.CID_GAME_CHIEF_DECIDE_SPEAK, title, param, "右", "左");
    }


    public void voteChief(Activity activity, Room room) {
        List<Integer> speakers = room.getPoliceList();
        Map<String, Integer> param = new HashMap<>();
        param.put("fromId", room.getCurUserId());
        param.put("roomId", room.getRoomId());
        param.put("bout", room.getBout());
        String title = "竞选警长，请投票";
        DialogManager.showModalChoice(activity, title, StringUtils.trans2StrArr(speakers, ""), ProtocolConstant.CID_GAME_CHIEF_VOTE, param);

    }

    public void chiefSumTicket(Activity activity, Room room) {
        Map<String, Integer> param = new HashMap<>();
        param.put("fromId", room.getCurUserId());
        param.put("roomId", room.getRoomId());
        param.put("bout", room.getBout());
        String title = "警长请选择归票";
        DialogManager.showModalChoice(activity, title, StringUtils.trans2StrArr(room.getLiveList(), ""), ProtocolConstant.CID_GAME_CHIEF_SUM_TICKET, param);

    }

    public void vote(Activity activity, Room room) {
        List<Integer> speakers = room.getLiveList();
        Map<String, Integer> param = new HashMap<>();
        param.put("fromId", room.getCurUserId());
        param.put("roomId", room.getRoomId());
        param.put("bout", room.getBout());
        String title = "请投票";
        DialogManager.showModalChoice(activity, title, StringUtils.trans2StrArr(speakers, ""), ProtocolConstant.CID_GAME_VOTE, param);

    }

    public void commonSend(Activity activity, Room room, short cid) {
        Map<String, Integer> param = new HashMap<>();
        param.put("fromId", room.getCurUserId());
        param.put("roomId", room.getRoomId());
        param.put("bout", room.getBout());
        param.put("content", 0);
        IMClientUtil.sendMsg(ProtocolConstant.SID_GAME, cid, param);

    }

    /**
     * 展示等待对话框
     *
     * @param role 角色
     */
    private void showWaitDialog(Role role, long time) {
        Message msg_system_timer = new Message();
        msg_system_timer.what = GameStateMessage.COUNTDOWNTIMER;  //倒计时
        msg_system_timer.obj = new GameStateMessage(wolf, String.format(Constants.TEXT_WAIT_ACTION, role.getName()), time);
        handler.sendMessage(msg_system_timer);

    }

    public void doDawn(String role, int bout) {  //天亮流程
//        switch ()


    }

    public void stopGame() {
        isGameing = false;
    }

    //被决定的人
    private static List<Integer> nightDieList = new ArrayList<Integer>();
    private static int who;
    private static Prophet prophet;
    private static Wolf wolf;
    private static Witch witch;

    /**
     * 进入流程中的某阶段
     */


    private boolean witchSave(int number) {
        witch = null;
        for (Map.Entry<Integer, Role> entry : deployMap.entrySet()) {
            if (entry.getValue().getName().equals(Witch.NAME) && entry.getValue().isAlive()) {
                witch = (Witch) entry.getValue();
                break;
            }
        }
        if (witch != null && witch.hasPanacea()) {
            if (Role.ACTION_GIVE_UP == witch.doRoleAction()) {
                witch.usePanacea();
                LogUtil.e("女巫拯救了" + number);
                return true;
            }
        }
        return false;
    }


    public List<Integer> getAliveList() {
        return aliveList;
    }

    @Override
    public void onStateChange() {
        handler.sendEmptyMessage(GameStateMessage.STAGE_CHANGE);

        aliveList = GameAlloter.updateAliver(aliveList, deployMap);
        winer = GameAlloter.checkWinner(deployMap);
        String str_winer = null;
        if (winer == Role.WIN_NO) {
            return;
        }
        if (winer == Role.WIN_WOLF) {
            str_winer = "狼人";
            isGameing = false;
        }
        if (winer == Role.WIN_PERSON) {
            str_winer = "好人";
            isGameing = false;
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, Role> entry : deployMap.entrySet()) {
            sb.append("\n" + entry.getKey() + "号" + entry.getValue().getName() + "(" + entry.getValue().getState() + ")");
        }
        Message msg_system_chat_result = new Message();
        msg_system_chat_result.what = GameStateMessage.GAME_OVER;
        msg_system_chat_result.obj = new GameStateMessage(null, String.format(Constants.TEXT_OVER_RESULT, str_winer, sb.toString()));
        handler.sendMessage(msg_system_chat_result);
        LogUtil.e(sb.toString());

        Message msg_room_title = new Message();
        msg_room_title.what = GameStateMessage.ZHOUYE;
        msg_room_title.obj = new GameStateMessage(null, "");
    }

    private class GameTask {
        private static final int COUNT_DOWN_INTERVAL = 1000;
        private long millisInFuture;
        private Runnable runnable;
        private boolean isCanRandom;

        public GameTask(long millisInFuture, Runnable runnable) {
            this.millisInFuture = millisInFuture;
            this.runnable = runnable;
        }

        public GameTask(long millisInFuture, Runnable runnable, boolean isCanRandom) {
            this.millisInFuture = millisInFuture;
            this.runnable = runnable;
            this.isCanRandom = isCanRandom;
        }


    }
}
