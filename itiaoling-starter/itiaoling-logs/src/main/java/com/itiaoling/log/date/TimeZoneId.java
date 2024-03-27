package com.itiaoling.log.date;

import java.time.ZoneId;


public final class TimeZoneId {

    private TimeZoneId() {}

    public static final ZoneId ZONE_LONDON = ZoneId.of("Europe/London");

    public static final ZoneId ZONE_SHANGHAI = ZoneId.of("Asia/Shanghai");

}
