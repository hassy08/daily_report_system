package actions.views;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 勤怠管理情報について画面の入力値・出力値を扱うViewモデル
 *
 */
@Getter //全てのクラスフィールドについてgetterを自動生成する(Lombok)
@Setter //全てのクラスフィールドについてsetterを自動生成する(Lombok)
@NoArgsConstructor //引数なしコンストラクタを自動生成する(Lombok)
@AllArgsConstructor //全てのクラスフィールドを引数にもつ引数ありコンストラクタを自動生成する(Lombok)
public class AttendanceView {

    /**
     * id
     */
    private Integer id;

    /**
     * 勤怠を登録した従業員
     */
    private EmployeeView employee;

    /**
     * いつの勤怠かを示す日付
     */
    private LocalDate attendanceDate;

    /**
     * 出勤時間
     */
    private LocalDateTime startedAt;

    /**
     * 退勤日時
     */
    private LocalDateTime leavedAt;
}
