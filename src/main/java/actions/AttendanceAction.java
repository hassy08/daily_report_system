package actions;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.AttendanceView;
import actions.views.EmployeeView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import constants.MessageConst;
import services.AttendanceService;

/**
 * 勤怠に関する処理を行うActionクラス
 *
 */
public class AttendanceAction extends ActionBase {

    private AttendanceService service;

    /**
     * メソッドを実行する
     */
    @Override
    public void process() throws ServletException, IOException {

        service = new AttendanceService();

        //メソッドを実行
        invoke();
        service.close();
    }

    /**
     * 一覧画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void index() throws ServletException, IOException {

        //指定されたページ数の一覧画面に表示する勤怠データを取得
        int page = getPage();
        List<AttendanceView> attendances = service.getAllPerPage(page);

        //勤怠データの件数を取得
        long attendancesCount = service.countAll();

        putRequestScope(AttributeConst.ATTENDANCES, attendances); //取得した勤怠データ
        putRequestScope(AttributeConst.ATT_COUNT, attendancesCount); //全ての勤怠データの件数
        putRequestScope(AttributeConst.PAGE, page); //ページ数
        putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE); //1ページに表示するレコードの数

        //セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
        String flush = getSessionScope(AttributeConst.FLUSH);
        if (flush != null) {
            putRequestScope(AttributeConst.FLUSH, flush);
            removeSessionScope(AttributeConst.FLUSH);
        }

        //一覧画面を表示
        forward(ForwardConst.FW_ATT_INDEX);
    }

    /**
     * 新規登録画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void entryNew() throws ServletException, IOException {

        putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン

        //勤怠情報の空インスタンスに、勤怠の日付＝今日の日付を設定する
        AttendanceView av = new AttendanceView();
        av.setAttendanceDate(LocalDate.now());
        putRequestScope(AttributeConst.ATTENDANCE, av); //日付のみ設定済みの勤怠インスタンス

        //新規登録画面を表示
        forward(ForwardConst.FW_ATT_NEW);

    }
    /**
     * 新規登録を行う
     * @throws ServletException
     * @throws IOException
     */
    public void create() throws ServletException, IOException {

        //CSRF対策 tokenのチェック
        if (checkToken()) {

            //勤怠の日付が入力されていなければ、今日の日付を設定
            LocalDate day = null;
            if (getRequestParam(AttributeConst.ATT_DATE) == null
                    || getRequestParam(AttributeConst.ATT_DATE).equals("")) {
                day = LocalDate.now();
            } else {
                day = LocalDate.parse(getRequestParam(AttributeConst.ATT_DATE));
            }

            LocalTime time1 = LocalTime.parse(getRequestParam(AttributeConst.ATT_STARTED_AT));
            LocalTime time2 = LocalTime.parse(getRequestParam(AttributeConst.ATT_LEAVED_AT));
            LocalDateTime datetime1 = LocalDateTime.of(day, time1);
            LocalDateTime datetime2 = LocalDateTime.of(day, time2);

            //セッションからログイン中の従業員情報を取得
            EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

            //パラメータの値をもとに勤怠情報のインスタンスを作成する
            AttendanceView av = new AttendanceView(
                    null,
                    ev, //ログインしている従業員を、勤怠作成者として登録する
                    day,
                    datetime1,
                    datetime2
                    );

            //勤怠情報登録
            List<String> errors = service.create(av);

            if (errors.size() > 0) {
                //登録中にエラーがあった場合

                putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
                putRequestScope(AttributeConst.ATTENDANCE, av);//入力された勤怠情報
                putRequestScope(AttributeConst.ERR, errors);//エラーのリスト

                //新規登録画面を再表示
                forward(ForwardConst.FW_ATT_NEW);

            } else {
                //登録中にエラーがなかった場合

                //セッションに登録完了のフラッシュメッセージを設定
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_REGISTERED.getMessage());

                //一覧画面にリダイレクト
                redirect(ForwardConst.ACT_ATT, ForwardConst.CMD_INDEX);
            }
        }
    }
    /**
     * 詳細画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void show() throws ServletException, IOException {

        //idを条件に勤怠データを取得する
        AttendanceView av = service.findOne(toNumber(getRequestParam(AttributeConst.ATT_ID)));

        if (av == null) {
            //該当の勤怠データが存在しない場合はエラー画面を表示
            forward(ForwardConst.FW_ERR_UNKNOWN);

        } else {

            putRequestScope(AttributeConst.ATTENDANCE, av); //取得した勤怠データ

            //詳細画面を表示
            forward(ForwardConst.FW_ATT_SHOW);
        }
    }

    /**
     * 編集画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void edit() throws ServletException, IOException {

        //idを条件に勤怠データを取得する
        AttendanceView av = service.findOne(toNumber(getRequestParam(AttributeConst.ATT_ID)));
        System.out.println("ATT_ID_1=" + getRequestParam(AttributeConst.ATT_ID));

        //セッションからログイン中の従業員情報を取得
        EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

        if (av == null || ev.getId() != av.getEmployee().getId()) {
            //該当の勤怠データが存在しない、または
            //ログインしている従業員が勤怠の作成者でない場合はエラー画面を表示
            forward(ForwardConst.FW_ERR_UNKNOWN);

        } else {

            putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
            putRequestScope(AttributeConst.ATTENDANCE, av); //取得した勤怠データ

            //編集画面を表示
            forward(ForwardConst.FW_ATT_EDIT);
        }

    }

    /**
     * 更新を行う
     * @throws ServletException
     * @throws IOException
     */
    public void update() throws ServletException, IOException {

        //CSRF対策 tokenのチェック
        if (checkToken()) {


            //idを条件に勤怠データを取得する
            AttendanceView av = service.findOne(toNumber(getRequestParam(AttributeConst.ATT_ID)));
            //AttendanceView av = service.findOne(toNumber(getRequestParam(AttributeConst.ATT_ID)));
            System.out.println("ATT_ID=" + getRequestParam(AttributeConst.ATT_ID));
            System.out.println(getRequestParam(AttributeConst.ATT_DATE));
            System.out.println("toLocaldate="+toLocalDate(getRequestParam(AttributeConst.ATT_DATE)));

            //入力された勤怠内容を設定する
            av.setAttendanceDate(toLocalDate(getRequestParam(AttributeConst.ATT_DATE)));
            av.setStartedAt(toLocalDatetime(getRequestParam(AttributeConst.ATT_STARTED_AT)));
            av.setLeavedAt(toLocalDatetime(getRequestParam(AttributeConst.ATT_LEAVED_AT)));

            //日報データを更新する
            List<String> errors = service.update(av);

            if (errors.size() > 0) {
                //更新中にエラーが発生した場合

                putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
                putRequestScope(AttributeConst.ATTENDANCE, av); //入力された勤怠情報
                putRequestScope(AttributeConst.ERR, errors); //エラーのリスト

                //編集画面を再表示
                forward(ForwardConst.FW_ATT_EDIT);
            } else {
                //更新中にエラーがなかった場合

                //セッションに更新完了のフラッシュメッセージを設定
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_UPDATED.getMessage());

                //一覧画面にリダイレクト
                redirect(ForwardConst.ACT_ATT, ForwardConst.CMD_INDEX);

            }
        }
    }
}