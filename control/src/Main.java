import entity.User;
import service.ConsoleUtil;
import service.LibraryService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Main {
    static LibraryService lib = new LibraryService();
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) {
        while(true){
            ConsoleUtil.printLine();
            System.out.println("===== 图书管理系统 =====");
            System.out.println("1 用户登录");
            System.out.println("2 用户注册");
            System.out.println("0 退出");
            int op = ConsoleUtil.readInt("请选择：");
            if(op == 0) break;
            if(op == 1){
                String user = ConsoleUtil.readLine("用户名：");
                String pwd = ConsoleUtil.readLine("密码：");
                User u = lib.login(user,pwd);
                if(u == null){
                    System.out.println("账号密码错误");
                    continue;
                }
                System.out.println("登录成功！");
                if(u.isAdmin()){
                    adminMenu(u);
                }else{
                    userMenu(u);
                }
            }else if(op ==2){
                String un = ConsoleUtil.readLine("用户名：");
                String pw = ConsoleUtil.readLine("密码：");
                String mj = ConsoleUtil.readLine("专业：");
                boolean ok = lib.register(un,pw,mj);
                if(ok) System.out.println("注册成功");
                else System.out.println("用户名已存在");
            }
        }
        System.out.println("系统退出");
    }

    // ========= 管理员菜单 =========
    static void adminMenu(User admin){
        while(true){
            ConsoleUtil.printLine();
            System.out.println("===== 管理员界面 =====");
            System.out.println("1 添加图书");
            System.out.println("2 查询图书");
            System.out.println("3 查询用户");
            System.out.println("4 查询借阅信息");
            System.out.println("5 借阅数据统计");
            System.out.println("0 返回登录");
            int op = ConsoleUtil.readInt("选择功能：");
            if(op ==0) break;
            switch (op){
                case 1:
                    String bid = ConsoleUtil.readLine("图书ID：");
                    String bname = ConsoleUtil.readLine("书名：");
                    String btype = ConsoleUtil.readLine("图书类型：");
                    lib.addBook(bid,bname,btype);
                    System.out.println("添加完成！");
                    lib.showBookList(lib.bookList);
                    break;
                case 2:
                    String bkw = ConsoleUtil.readLine("输入图书关键词(直接回车查全部)：");
                    List<entity.Book> bkList = lib.searchBook(bkw);
                    lib.showBookList(bkList);
                    break;
                case 3: {
                    String ukw = ConsoleUtil.readLine("输入用户关键词(直接回车查全部)：");
                    List<User> ulist = lib.searchUser(ukw);
                    lib.showUserList(ulist);
                    break;
                }

                case 4:
                borrowRecordAdminMenu();
                break;
                case 5:
                System.out.println("==== 按图书类型统计借阅次数(降序) ====");
                Map<String,Integer> typeStat = lib.statByBookType();
                for(Map.Entry<String,Integer> entry:typeStat.entrySet()){
                    System.out.println(entry.getKey()+"："+entry.getValue()+"次");
                }
                System.out.println("\n==== 按用户专业统计借阅次数(降序) ====");
                Map<String,Integer> majorStat = lib.statByMajor();
                for(Map.Entry<String,Integer> entry:majorStat.entrySet()){
                    System.out.println(entry.getKey()+"："+entry.getValue()+"次");
                }
                break;
            }
        }
    }

    static void borrowRecordAdminMenu(){
        while(true){
            ConsoleUtil.printLine();
            System.out.println("借阅信息查询");
            System.out.println("1 关键字查询");
            System.out.println("2 查询超期借阅");
            System.out.println("3 日期范围查询");
            System.out.println("0 返回");
            int op = ConsoleUtil.readInt("选择：");
            if(op ==0) break;
            if(op ==1){
                String kw = ConsoleUtil.readLine("关键词：");
                lib.showRecordList(lib.searchRecordByKeyword(kw));
            }else if(op ==2){
                lib.showRecordList(lib.searchOverdueRecord());
            }else if(op ==3){
                try {
                    String sStr = ConsoleUtil.readLine("起始日期 yyyy-MM-dd：");
                    String eStr = ConsoleUtil.readLine("结束日期 yyyy-MM-dd：");
                    Date s = sdf.parse(sStr);
                    Date e = sdf.parse(eStr);
                    lib.showRecordList(lib.searchRecordByDate(s,e));
                } catch (ParseException ex) {
                    System.out.println("日期格式错误");
                }
            }
        }
    }

    // ========= 普通借阅用户菜单 =========
    static void userMenu(User user){
        String username = user.getUsername();
        while(true){
            ConsoleUtil.printLine();
            System.out.println("===== 用户借阅界面 =====");
            System.out.println("1 借书");
            System.out.println("2 还书");
            System.out.println("3 查询个人借阅");
            System.out.println("0 返回登录");
            int op = ConsoleUtil.readInt("选择：");
            if(op ==0) break;
            switch (op){
                case 1:
                    borrowBookMenu(username);
                    break;
                case 2:
                returnBookMenu(username);
                break;
                case 3:
                userQueryMenu(username);
                break;
            }
        }
    }

    static void borrowBookMenu(String username){
        while(true){
            ConsoleUtil.printLine();
            System.out.println("===== 借书功能 =====");
            System.out.println("【你的未还借阅】");
            lib.showRecordList(lib.getUserUnReturnRecord(username));
            System.out.println("【可借阅图书列表】");
            List<entity.Book> canBorrow = lib.bookList.stream().filter(b->b.isCanBorrow()).toList();
            lib.showBookList(canBorrow);
            String bid = ConsoleUtil.readLine("输入图书ID借书，输入exit退出借书：");
            if("exit".equals(bid)) break;
            boolean ok = lib.borrowBook(username,bid);
            if(ok){
                System.out.println("借阅成功！");
            }else{
                System.out.println("借阅失败，图书不存在或已借出");
            }
        }
    }

    static void returnBookMenu(String username){
        ConsoleUtil.printLine();
        System.out.println("===== 还书功能 =====");
        System.out.println("你的未归还借阅：");
        List<entity.BorrowRecord> unRet = lib.getUserUnReturnRecord(username);
        lib.showRecordList(unRet);
        String bid = ConsoleUtil.readLine("输入要归还的图书ID：");
        boolean ok = lib.returnBook(username,bid);
        if(ok) System.out.println("归还成功");
        else System.out.println("归还失败");
    }

    static void userQueryMenu(String username){
        while(true){
            ConsoleUtil.printLine();
            System.out.println("=====个人借阅查询 =====");
            System.out.println("1 查看全部借阅");
            System.out.println("2 查看未还借阅");
            System.out.println("3 日期范围查询");
            System.out.println("0 返回");
            int op = ConsoleUtil.readInt("选择：");
            if(op ==0) break;
            if(op ==1){
                lib.showRecordList(lib.getUserAllRecord(username));
            }else if(op ==2){
                lib.showRecordList(lib.getUserUnReturnRecord(username));
            }else if(op ==3){
                try {
                    String sStr = ConsoleUtil.readLine("起始日期 yyyy-MM-dd：");
                    String eStr = ConsoleUtil.readLine("结束日期 yyyy-MM-dd：");
                    Date s = sdf.parse(sStr);
                    Date e = sdf.parse(eStr);
                    List<entity.BorrowRecord> all = lib.getUserAllRecord(username);
                    List<entity.BorrowRecord> res = all.stream()
                            .filter(r->{
                                Date d = r.getBorrowDate();
                                return d.after(s) && d.before(e);
                            }).toList();
                    lib.showRecordList(res);
                } catch (ParseException ex) {
                    System.out.println("日期格式错误");
                }
            }
        }
    }
}
