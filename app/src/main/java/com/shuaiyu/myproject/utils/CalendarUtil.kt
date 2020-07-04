package com.shuaiyu.myproject.utils

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.provider.CalendarContract
import android.text.TextUtils
import android.util.Log
import com.hjq.toast.ToastUtils
import java.util.*


object CalendarUtil {

    private var CALENDAR_URL:String  = "content://com.android.calendar/calendars";
    private var CALENDAR_EVENT_URL: String  = "content://com.android.calendar/events";
    private var CALENDAR_REMINDER_URL:String  = "content://com.android.calendar/reminders";


    private var CALENDARS_NAME: String  = "mang";
    private var CALENDARS_ACCOUNT_NAME: String  = "mang@mmd.com";
    private var CALENDARS_ACCOUNT_TYPE: String  = "com.android.mang";
    private var CALENDARS_DISPLAY_NAME: String  = "mang";


    fun checkAndAddCalendarAccount(context: Context) :Int{
       var id = checkCalendarAccount(context);
        Log.e("TAG", "checkAndAddCalendarAccount: $id")
        if (id != -1) return id;
        else {
            Log.e("TAG", "addCalendarAccount: ${addCalendarAccount(context)}")
            return addCalendarAccount(context);
        }
    }


    /**
     * 检查现在是否已经存在日历账户
     */
   fun checkCalendarAccount(context: Context) :Int{
       val userCursor = context.getContentResolver().query(
           Uri.parse(CALENDAR_URL),
            null, null, null, null);
        try {
            if (userCursor == null) { // 查询返回空值
                return -1;
            }
            val count = userCursor.getCount();
            if (count > 0) { // 存在现有账户，取第一个账户的id返回
                userCursor.moveToFirst();
                return userCursor.getInt(userCursor.getColumnIndex(CalendarContract.Calendars._ID));
            } else {
                return -1;
            }
        } finally {
            if (userCursor != null) {
                userCursor.close();
            }
        }
    }

    /**
     * 添加账户
     */
    fun addCalendarAccount(context:Context ): Int {
        var timeZone = TimeZone.getDefault();
        val value = ContentValues();
        value.put(CalendarContract.Calendars.NAME, CALENDARS_NAME);
        value.put(CalendarContract.Calendars.ACCOUNT_NAME, CALENDARS_ACCOUNT_NAME);
        value.put(CalendarContract.Calendars.ACCOUNT_TYPE, CALENDARS_ACCOUNT_TYPE);
        value.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, CALENDARS_DISPLAY_NAME);
        value.put(CalendarContract.Calendars.VISIBLE, 1);
        value.put(CalendarContract.Calendars.CALENDAR_COLOR, Color.BLUE);
        value.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL,
            CalendarContract.Calendars.CAL_ACCESS_OWNER);
        value.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
        value.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, timeZone.getID());
        value.put(CalendarContract.Calendars.OWNER_ACCOUNT, CALENDARS_ACCOUNT_NAME);
        value.put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, 0);

        var calendarUri = Uri.parse(CALENDAR_URL);
        calendarUri = calendarUri.buildUpon()
            .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
            .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME,
                CALENDARS_ACCOUNT_NAME)
            .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE,
                CALENDARS_ACCOUNT_TYPE)
            .build();

        var result = context.getContentResolver().insert(calendarUri, value);
         var id:Int
        if( result == null)
            id=-1
        else
            id= ContentUris.parseId(result).toInt()
        return id;
    }

    /**
     * title b标题
     * description 内容
     * 开始时间 结束时间
     */
    fun insertCalendarEvent(
        context: Context?, title: String?, description: String?,
        beginTimeMillis: Long, endTimeMillis: Long
    ): Boolean {
        var beginTimeMillis = beginTimeMillis
        var endTimeMillis = endTimeMillis
        if (context == null || TextUtils.isEmpty(title) || TextUtils.isEmpty(description)) {
            return false
        }
        val calId = checkAndAddCalendarAccount(context) // 获取日历账户的id
        if (calId < 0) { // 获取账户id失败直接返回，添加日历事件失败
            return false
        }

        // 如果起始时间为零，使用当前时间
        if (beginTimeMillis == 0L) {
            val beginCalendar = Calendar.getInstance()
            beginTimeMillis = beginCalendar.timeInMillis
        }
        // 如果结束时间为零，使用起始时间+30分钟
        if (endTimeMillis == 0L) {
            endTimeMillis = beginTimeMillis + 30 * 60 * 1000
        }
        try {
            /** 插入日程  */
            val eventValues = ContentValues()
            eventValues.put(CalendarContract.Events.DTSTART, beginTimeMillis)
            eventValues.put(CalendarContract.Events.DTEND, endTimeMillis)
            eventValues.put(CalendarContract.Events.TITLE, title)
            eventValues.put(CalendarContract.Events.DESCRIPTION, description)
            eventValues.put(CalendarContract.Events.CALENDAR_ID, 1)
            eventValues.put(CalendarContract.Events.EVENT_LOCATION, "蒙app")
            val tz = TimeZone.getDefault() // 获取默认时区
            eventValues.put(CalendarContract.Events.EVENT_TIMEZONE, tz.id)
            val eUri = context.contentResolver
                .insert(Uri.parse(CALENDAR_EVENT_URL), eventValues)
            val eventId = ContentUris.parseId(eUri)
            if (eventId == 0L) { // 插入失败
                return false
            }
            /** 插入提醒 - 依赖插入日程成功  */
            val reminderValues = ContentValues()
            // uri.getLastPathSegment();
            reminderValues.put(CalendarContract.Reminders.EVENT_ID, eventId)
            reminderValues.put(CalendarContract.Reminders.MINUTES, 10) // 提前10分钟提醒
            reminderValues.put(
                CalendarContract.Reminders.METHOD,
                CalendarContract.Reminders.METHOD_ALERT
            )
            val rUri = context.contentResolver.insert(
                Uri.parse(CALENDAR_REMINDER_URL),
                reminderValues
            )
            Log.e("TAG", "insertCalendarEvent: " )
            if (rUri == null || ContentUris.parseId(rUri) == 0L) {
                return false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return true
    }

    /**
     * 删除事件
     */
    fun deleteCalendarEvent(
        context: Context?,
        title: String
    ) {
        if (context == null) {
            return
        }
        val eventCursor: Cursor? = context.contentResolver.query(
            Uri.parse(CALENDAR_EVENT_URL),
            null, null, null, null
        )
        try {
            if (eventCursor == null) { // 查询返回空值
                return
            }
            if (eventCursor.getCount() > 0) {
                // 遍历所有事件，找到title跟需要查询的title一样的项
                eventCursor.moveToFirst()
                while (!eventCursor.isAfterLast()) {
                    val eventTitle: String =
                        eventCursor.getString(eventCursor.getColumnIndex("title"))
                    if (!TextUtils.isEmpty(title) && title == eventTitle) {
                        val id: Int = eventCursor.getInt(
                            eventCursor
                                .getColumnIndex(CalendarContract.Calendars._ID)
                        ) // 取得id
                        val deleteUri = ContentUris.withAppendedId(
                            Uri.parse(CALENDAR_EVENT_URL),
                            id.toLong()
                        )
                        val rows = context.contentResolver.delete(deleteUri, null, null)
                        if (rows == -1) { // 事件删除失败
                            ToastUtils.show("失败")
                            return
                        }
                    }
                    eventCursor.moveToNext()
                }
            }
            ToastUtils.show("结束")
        } finally {
            if (eventCursor != null) {
                eventCursor.close()
            }
        }
    }
}