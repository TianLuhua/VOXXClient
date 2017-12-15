package com.sat.satcontorl;

public class Setting {

    public static class PortGlob {
        public static final int MULTIPORT = 9696;
        public static final int DATAPORT = 8686;
        public static final int TOUCHPORT = 8181;
        public static final int BACKPORT = 9191;

    }

    public static class MediaCodecGlod {
        public final static String MIME_TYPE = "video/avc"; // H.264 Advanced
        public final static int VIDEO_WIDTH = 360;
        public final static int VIDEO_HEIGHT = 640;
        public final static int FRAME_RATE = 24;
        public final static int FRAME_INTERVAL = 1;
        public static final int VFPS = 24;
        public static final int VGOP = 48;
        public final static int FRAME_BIT_RATE = 500 * 1024;

    }

    public static class HandlerGlod {
        public static final int CONNET_SUCCESS = 1;
        public static final int SCAN_IP_OVER = 2;
        public static final int CLEAR_FAILCOUNT = 3;
        public static final int TIME_OUT = 4;
        public static final int CONNECT_FAIL = 5;
        public static final int START_SHOW_DATA = 6;
    }

    public static class MotionEventKey {
        public static final String JACTION = "action";
        public static final String JX = "x";
        public static final String JY = "y";
    }

}
