package service;

import entity.Book;
import entity.BorrowRecord;
import entity.User;

import java.util.*;
import java.util.stream.Collectors;

public class LibraryService {
    public List<User> userList = new ArrayList<>();
    public List<Book> bookList = new ArrayList<>();
    public List<BorrowRecord> recordList = new ArrayList<>();

    public LibraryService() {
        // 默认管理员账号 admin / 123456
        userList.add(new User("admin", "123456", true, "管理员"));
    }

    // ========= 用户注册登录 =========
    public boolean register(String username, String pwd, String major) {
        for (User u : userList) {
            if (u.getUsername().equals(username)) return false;
        }
        userList.add(new User(username, pwd, false, major));
        return true;
    }

    public User login(String username, String pwd) {
        for (User u : userList) {
            if (u.getUsername().equals(username) && u.getPassword().equals(pwd)) {
                return u;
            }
        }
        return null;
    }

    // ========= 管理员：添加图书 =========
    public void addBook(String bid, String bname, String btype) {
        bookList.add(new Book(bid, bname, btype));
    }

    // 打印图书列表
    public void showBookList(List<Book> list) {
        ConsoleUtil.printLine();
        System.out.printf("%-10s %-20s %-10s %-8s%n", "图书ID", "书名", "类型", "是否可借");
        for (Book b : list) {
            String status = b.isCanBorrow() ? "可借" : "已借出";
            System.out.printf("%-10s %-20s %-10s %-8s%n", b.getBookId(), b.getBookName(), b.getBookType(), status);
        }
        ConsoleUtil.printLine();
    }

    // 管理员：图书关键词查询
    public List<Book> searchBook(String keyword) {
        if (keyword.isEmpty()) return bookList;
        return bookList.stream()
                .filter(b -> b.getBookId().contains(keyword) || b.getBookName().contains(keyword) || b.getBookType().contains(keyword))
                .collect(Collectors.toList());
    }

    // 管理员：用户关键词查询
    public List<User> searchUser(String keyword) {
        if (keyword.isEmpty()) return userList;
        return userList.stream()
                .filter(u -> u.getUsername().contains(keyword) || u.getMajor().contains(keyword))
                .collect(Collectors.toList());
    }

    public void showUserList(List<User> list) {
        ConsoleUtil.printLine();
        System.out.printf("%-12s %-10s %-10s%n", "用户名", "角色", "专业");
        for (User u : list) {
            String role = u.isAdmin() ? "管理员" : "借阅用户";
            System.out.printf("%-12s %-10s %-10s%n", u.getUsername(), role, u.getMajor());
        }
        ConsoleUtil.printLine();
    }

    // ========= 管理员：借阅信息查询 =========
    // 1.关键字查借阅记录
    public List<BorrowRecord> searchRecordByKeyword(String keyword) {
        if (keyword.isEmpty()) return recordList;
        return recordList.stream()
                .filter(r -> r.getUsername().contains(keyword) || r.getBook().getBookId().contains(keyword) || r.getBook().getBookName().contains(keyword))
                .collect(Collectors.toList());
    }

    // 2.查询超期借阅
    public List<BorrowRecord> searchOverdueRecord() {
        return recordList.stream()
                .filter(r -> r.getReturnDate() == null && r.getOverdueDays() > 0)
                .collect(Collectors.toList());
    }

    //3.日期范围查询借阅
    public List<BorrowRecord> searchRecordByDate(Date start, Date end) {
        return recordList.stream()
                .filter(r -> {
                    Date d = r.getBorrowDate();
                    return d.after(start) && d.before(end);
                }).collect(Collectors.toList());
    }

    public void showRecordList(List<BorrowRecord> list) {
        ConsoleUtil.printLine();
        System.out.printf("%-10s %-12s %-20s %-12s %-12s%n", "记录ID", "用户名", "书名", "借阅日期", "归还日期");
        for (BorrowRecord r : list) {
            String ret = r.getReturnDate() == null ? "未归还" : r.getReturnDate().toString();
            System.out.printf("%-10s %-12s %-20s %-12s %-12s%n",
                    r.getRecordId(), r.getUsername(), r.getBook().getBookName(), r.getBorrowDate(), ret);
        }
        ConsoleUtil.printLine();
    }

    // ========= 管理员：统计功能 =========
    // 1.按图书类型统计借阅次数，降序
    public Map<String, Integer> statByBookType() {
        Map<String, Integer> map = new HashMap<>();
        for (BorrowRecord r : recordList) {
            String type = r.getBook().getBookType();
            map.put(type, map.getOrDefault(type, 0) + 1);
        }
        // 排序
        return map.entrySet().stream()
                .sorted((a,b)->b.getValue().compareTo(a.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (o,n)->o, LinkedHashMap::new));
    }

    //2.按用户专业统计借阅次数，降序
    public Map<String, Integer> statByMajor() {
        Map<String, Integer> map = new HashMap<>();
        for (BorrowRecord r : recordList) {
            String uname = r.getUsername();
            Optional<User> userOpt = userList.stream().filter(u->u.getUsername().equals(uname)).findFirst();
            if(userOpt.isPresent()){
                String major = userOpt.get().getMajor();
                map.put(major, map.getOrDefault(major,0)+1);
            }
        }
        return map.entrySet().stream()
                .sorted((a,b)->b.getValue().compareTo(a.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (o,n)->o, LinkedHashMap::new));
    }

    // ========= 借阅用户功能 =========
    // 获取用户未还图书
    public List<BorrowRecord> getUserUnReturnRecord(String username) {
        return recordList.stream()
                .filter(r -> r.getUsername().equals(username) && r.getReturnDate() == null)
                .collect(Collectors.toList());
    }

    // 用户全部借阅记录
    public List<BorrowRecord> getUserAllRecord(String username) {
        return recordList.stream()
                .filter(r -> r.getUsername().equals(username))
                .collect(Collectors.toList());
    }

    // 用户借阅：输入图书ID借书
    public boolean borrowBook(String username, String bookId) {
        for(Book b : bookList){
            if(b.getBookId().equals(bookId) && b.isCanBorrow()){
                b.setCanBorrow(false);
                String rid = UUID.randomUUID().toString().substring(0,8);
                BorrowRecord br = new BorrowRecord(rid, username, b, new Date());
                recordList.add(br);
                return true;
            }
        }
        return false;
    }

    // 用户还书：输入图书ID归还
    public boolean returnBook(String username, String bookId) {
        for(BorrowRecord r : recordList){
            if(r.getUsername().equals(username) && r.getReturnDate()==null && r.getBook().getBookId().equals(bookId)){
                r.setReturnDate(new Date());
                r.getBook().setCanBorrow(true);
                return true;
            }
        }
        return false;
    }
}
