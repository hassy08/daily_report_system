package models.validators;

import java.util.ArrayList;
import java.util.List;

import actions.views.AttendanceView;

/**
 * 日報インスタンスに設定されている値のバリデーションを行うクラス
 */
public class AttendanceValidator {

    /**
     * 勤怠インスタンスの各項目についてバリデーションを行う
     * @param av 勤怠インスタンス
     * @return エラーのリスト
     */
    public static List<String> validate(AttendanceView av) {
        List<String> errors = new ArrayList<String>();

        return errors;


    }


}