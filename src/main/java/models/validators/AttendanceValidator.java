package models.validators;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import actions.views.AttendanceView;
import constants.MessageConst;

/**
 * 勤怠インスタンスに設定されている値のバリデーションを行うクラス
 */
public class AttendanceValidator {

    /**
     * 勤怠インスタンスの各項目についてバリデーションを行う
     * @param av 勤怠インスタンス
     * @return エラーのリスト
     */
    public static List<String> validate(AttendanceView av) {
        List<String> errors = new ArrayList<String>();


        //出勤時間のチェック
        String time1 = av.getStartedAt().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String startedAtError = validateStartedAt(time1);
        if (!startedAtError.equals("")) {
            errors.add(startedAtError);
        }

        //内容のチェック
        String time2 = av.getLeavedAt().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String leavedAtError = validateLeavedAt(time2);
        if (!leavedAtError.equals("")) {
            errors.add(leavedAtError);
        }

        return errors;
    }

    /**
     * 出勤時間に入力値があるかをチェックし、入力値がなければエラーメッセージを返却
     * @param startedAt 出勤時間
     * @return エラーメッセージ
     */
    private static String validateStartedAt(String time1) {
        if (time1 == null || time1.equals("")) {
            return MessageConst.E_NOSTARTED_AT.getMessage();
        }

        //入力値がある場合は空文字を返却
        return "";
    }

    /**
     * 退勤時間に入力値があるかをチェックし、入力値がなければエラーメッセージを返却
     * @param leavedAt 退勤時間
     * @return エラーメッセージ
     */
    private static String validateLeavedAt(String time2) {
        if (time2 == null || time2.equals("")) {
            return MessageConst.E_NOLEAVED_AT.getMessage();
        }

        //入力値がある場合は空文字を返却
        return "";
    }



}