import java.io.*;
import java.io.File;
import java.util.Date;
import java.text.SimpleDateFormat;
public class InfectStatistic {
    //日志地址
    public String logaddr;
    public String resultaddr;

    //省份
    public String[] province={"全国","安徽","北京","重庆","福建","甘肃",
            "广东","广西","贵州","海南","河北","河南","黑龙江","湖北",
            "湖南","吉林","江苏","江西","辽宁","内蒙古","宁夏","青海",
            "山东","山西","陕西","上海","四川","天津","西藏", "新疆",
            "云南","浙江","澳门","香港","台湾"};//各省名称
    public int[] is_province=new int[35];//统计各省是否需要输出
    public int[][] person = new int[35][4];//各省各类人群

    //list命令参数
    public String[] li_cmd={"-log","-out","-date","-type","-province"};

    //感染类型
    public String[] type={"ip","sp","cure","dead"};//感染类型
    public String[] cntype={"感染患者", "疑似患者", "治愈", "死亡"};//感染类型
    public int[] in_type={1,1,1,1};//0不输出，为1输出

    //获取当前时间
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Date d = new Date(System.currentTimeMillis());//获取当前时间
    public String date_now = formatter.format(d); //转化格式

    //判断是否输出其它省
    public int flag=0;

    //解析输入命令
    class Command {
        String[] cmd; //保存命令行

        Command(String[] args) {
            cmd = args;
            is_province[0] = 0;//全国默认
        }

        //解析命令
        public boolean isrt_cmd() {
            if (!cmd[0].equals("list")) {//判断命令格式开头是否正确
                System.out.println("命令开头非list，错误");
                return false;
            }
            int i;
            for (i = 1; i < cmd.length; i++) {//参数验证
                if (cmd[i].equals(li_cmd[0])) {
                    i++;//读取地址
                    i = get_Logaddr(i);
                    if(i == -1) {
                        System.out.println("输入日志路径参数错误");
                        return false;
                    }

                } else if (cmd[i].equals(li_cmd[1])) {
                    i++;//读取地址
                    i = get_Resultaddr(i);
                    if(i == -1) {
                        System.out.println("输出日志路径参数错误");
                        return false;
                    }

                } else if (cmd[i].equals(li_cmd[2])) {
                    i++;
                    i = get_Date(i);
                    if(i == -1) { //说明上述步骤中发现命令行出错
                        System.out.println("日期参数错误");
                        return false;
                    }


                } else if (cmd[i].equals(li_cmd[3])) {
                    i++;
                    i = get_Type(i);
                    if(i == -1) { //说明上述步骤中发现命令行出错
                        System.out.println("患者参数错误");
                        return false;
                    }

                } else if (cmd[i].equals(li_cmd[4])) {
                    i++;
                    i = get_Province(i);
                    if(i == -1) { //说明上述步骤中发现命令行出错
                        System.out.println("省份参数错误");
                        return false;
                    }

                }else{
                    System.out.println("无该参数，请检查输入是否错误");
                    return false;
                }
            }
            return true;

        }

        //判断输入地址是否正确
        public boolean inaddr(String path) {
            if (path.matches("^[A-z]:\\\\(.+?\\\\)*$"))
                return true;
            else
                return false;
        }

        //判断输出地址是否正确
        public boolean outaddr(String path){
            if (path.matches("^[A-z]:\\\\(\\S+)+(\\.txt)$"))
                return true;
            else
                return false;
        }

        //-log,输入日志文件地址
        public int get_Logaddr(int t) {
            if (t < cmd.length) {
                if(inaddr(cmd[t])) //判断是不是文件目录路径
                    logaddr = cmd[t];
                else
                    return -1;
            } else
                return -1;
            return t;
        }

        //-out,输出日志地址
        public int get_Resultaddr(int t) {
            if (t < cmd.length) {
                if(outaddr(cmd[t])) //判断是不是文件目录路径
                    resultaddr = cmd[t];
                else
                    return -1;
            } else
                return -1;
            return t;
        }


        //判断日期格式是否合理
        public boolean isRtdate(String indate) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                if (indate.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
                    Date date1 = format.parse(indate);
                    if (indate.equals(format.format(date1))) {
                        return true;
                    }else
                        return false;
                }
                else
                    return false;
            }catch (Exception e) {
                return false;
            }
        }

        //-data,获取指定日期
        public int get_Date(int t) {
            if (t < cmd.length) {
                if(isRtdate(cmd[t])){//判断日期格式是否合法
                    if (date_now.compareTo(cmd[t]) >= 0) {//当前日期与输出日期比较
                        date_now = cmd[t] + ".log.txt";//修改文件名
                    } else
                        return -1;
                }else
                    return -1;
            } else
                return -1;
            return t;
        }

        //-type,获取指定类型
        public int get_Type(int t) {
            int n=t-1;
            if (t < cmd.length) {
                for (int i = 0; i < 4; i++)//默认为不输出
                {
                    in_type[i] = 0;
                }
                while (t < cmd.length) {
                    if (cmd[t].equals(type[0])) {
                        in_type[0] = 1;
                        t++;
                    } else if (cmd[t].equals(type[1])) {
                        in_type[1] = 1;
                        t++;
                    } else if (cmd[t].equals(type[2])) {
                        in_type[2] = 1;
                        t++;
                    } else if (cmd[t].equals(type[3])) {
                        in_type[3] = 1;
                        t++;
                    } else
                        break;
                }
            }
            t--;//返回前一个
            if (n == t) //type后面没有参数,输入错误
                return -1;
            return t;
        }

        //-province,获取指定省份
        public int get_Province(int t) {
            int n = t-1;
            int fg=0;
            if (t < cmd.length) {
                is_province[0] = 0;//取消默认
                while (t < cmd.length) {
                    for (int i = 0; i < province.length; i++) {//确定哪几个省份输出
                        if (cmd[t].equals(province[i])) {
                            is_province[i] = 1;
                            t++;
                            flag=1;
                            fg=1;
                            break;
                        }
                    }
                    if(fg==0) {//避免后面出现其他参数，直接跳出省份循环
                        break;
                    }
                    fg=0;
                }

            }
            t--;
            if (n == t)
                return -1;
            return t;

        }

    }


    //处理文件
    class File_handle {
        File_handle() {
        }


        //读取路径下的文件
        public void get_Flist() {
            File[] flist;
            String fname;
            File file = new File(logaddr);//获取指定目录下文件
            flist = file.listFiles();
            int i;
            for (i = 0; i < flist.length; i++) {
                fname = flist[i].getName();//文件名
                if (fname.compareTo(date_now) <= 0) {
                    String alladdr = logaddr + fname;
                    /*
                    用来检验是否找到符合要求的文件地址
                    System.out.println(alladdr);
                    */
                    Read_txt(alladdr);
                }
            }
        }

        //读取文本内容
        public void Read_txt(String address) {
            try {
                BufferedReader bf;
                bf = new BufferedReader(new InputStreamReader(
                        new FileInputStream(new File(address)), "UTF-8"));
                String rLine = null;
                /*隔行读取了
                while(br.readLine()!=null){
                    rLine=br.readLine();//按行读取
                    //if (!rLine.startsWith("//")){
                        System.out.println(rLine);
                    //}

                 */
                while ((rLine = bf.readLine()) != null) {
                    if (!rLine.startsWith("//")) {
                        /*
                        检测文档读取是否正确
                        System.out.println(rLine);
                        */
                        Deal_txt(rLine);
                    }
                }
                bf.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

        //处理文本
        public void Deal_txt(String line) {
            String[] str = {"(\\S+) 新增 感染患者 (\\d+)人", "(\\S+) 新增 疑似患者 (\\d+)人",
                    "(\\S+) 感染患者 流入 (\\S+) (\\d+)人", "(\\S+) 疑似患者 流入 (\\S+) (\\d+)人",
                    "(\\S+) 死亡 (\\d+)人", "(\\S+) 治愈 (\\d+)人",
                    "(\\S+) 疑似患者 确诊感染 (\\d+)人", "(\\S+) 排除 疑似患者 (\\d+)人"
            };//日志可能出现的情况
            int num[] = new int[8];//各种人数
            String[] txt_str = line.split(" ");//用空格隔开

            if (line.matches(str[0])) {//新增感染者
                txt_str[3] = txt_str[3].replace("人", "");
                num[0] = Integer.valueOf(txt_str[3]);//把人数转化为int类型
                for (int i = 0; i < province.length; i++) {
                    if (txt_str[0].equals(province[i])) {
                        //System.out.println(province[i]);
                        person[i][0] += num[0];
                        person[0][0] += num[0];
                        if(flag == 0) //省份未指定
                            is_province[i] = 1;
                        break;
                    }
                }
            } else if (line.matches(str[1])) {//新增疑似
                txt_str[3] = txt_str[3].replace("人", "");
                num[1] = Integer.valueOf(txt_str[3]);
                for (int i = 0; i < province.length; i++) {
                    if (txt_str[0].equals(province[i])) {
                        person[i][1] += num[1];
                        person[0][1] += num[1];
                        if(flag == 0) //省份未指定
                            is_province[i] = 1;
                        break;
                    }
                }
            } else if (line.matches(str[2])) {//感染流入
                txt_str[4] = txt_str[4].replace("人", "");
                num[2] = Integer.valueOf(txt_str[4]);
                for (int j = 0; j < province.length; j++) {
                    if (txt_str[0].equals(province[j])) {//流出省
                        person[j][0] -= num[2];
                        if(flag == 0) //省份未指定
                            is_province[j] = 1;
                        break;
                    }
                }
                for (int i = 0; i < province.length; i++) {
                    if (txt_str[3].equals(province[i])) {//流入省
                        person[i][0] += num[2];
                        if(flag == 0) //省份未指定
                            is_province[i] = 1;
                        break;
                    }
                }

            } else if (line.matches(str[3])) {//疑似流入
                txt_str[4] = txt_str[4].replace("人", "");
                num[3] = Integer.valueOf(txt_str[4]);
                for (int j = 0; j < province.length; j++) {//流出省
                    if (txt_str[0].equals(province[j])) {
                        person[j][1] -= num[3];
                        if(flag == 0) //省份未指定
                            is_province[j] = 1;
                        break;
                    }
                }
                for (int i = 0; i < province.length; i++) {//流入省
                    if (txt_str[3].equals(province[i])) {
                        person[i][1] += num[3];
                        if(flag == 0) //省份未指定
                            is_province[i] = 1;
                        break;
                    }
                }

            } else if (line.matches(str[4])) {//死亡人数
                txt_str[2] = txt_str[2].replace("人", "");
                num[4] = Integer.valueOf(txt_str[2]);
                for (int i = 0; i < province.length; i++) {
                    if (txt_str[0].equals(province[i])) {
                        person[i][3] += num[4];//该省死亡人数增加
                        person[0][3] += num[4];//全国死亡人数增加
                        person[i][0] -= num[4];//该省感染人数减少
                        person[0][0] -= num[4];//全国感染人数减少
                        if(flag == 0) //省份未指定
                            is_province[i] = 1;
                        break;
                    }
                }
            } else if (line.matches(str[5])) {//治愈人数
                txt_str[2] = txt_str[2].replace("人", "");
                num[5] = Integer.valueOf(txt_str[2]);
                for (int i = 0; i < province.length; i++) {
                    if (txt_str[0].equals(province[i])) {
                        person[i][2] += num[5];//该省治愈人数增加
                        person[0][2] += num[5];//全国治愈人数增加
                        person[i][0] -= num[5];//该省感染人数减少
                        person[0][0] -= num[5];//全国感染人数减少
                        if(flag == 0) //省份未指定
                            is_province[i] = 1;
                        break;
                    }
                }
            } else if (line.matches(str[6])) {//疑似确诊感染
                txt_str[3] = txt_str[3].replace("人", "");
                num[6] = Integer.valueOf(txt_str[3]);
                for (int i = 0; i < province.length; i++) {
                    if (txt_str[0].equals(province[i])) {
                        person[i][0] += num[6];//该省感染人数增加
                        person[0][0] += num[6];//全国感染人数增加
                        person[i][1] -= num[6];//该省疑似人数减少
                        person[0][1] -= num[6];//全国疑似人数减少
                        if(flag == 0) //省份未指定
                            is_province[i] = 1;
                        break;
                    }
                }
            } else if (line.matches(str[7])) {//排除疑似者
                txt_str[3] = txt_str[3].replace("人", "");
                num[7] = Integer.valueOf(txt_str[3]);
                for (int i = 0; i < province.length; i++) {
                    if (txt_str[0].equals(province[i])) {
                        person[i][1] -= num[7];//该省疑似人数减少
                        person[0][1] -= num[7];//全国疑似人数减少
                        if(flag == 0) //省份未指定
                            is_province[i] = 1;
                        break;
                    }
                }
            }
        }

        //输出文本
        public void Write_txt() {
            FileWriter fwrite;
            try {
                fwrite = new FileWriter(resultaddr);
                if(is_province[0]==0)
                    is_province[0]=1;
                for (int i = 0; i < province.length; i++) {
                    if (is_province[i] == 1) {
                        fwrite.write(province[i] + " ");
                        for (int j = 0; j < type.length; j++) {
                            if (in_type[j] == 1) {
                                fwrite.write(cntype[j] + " "+ person[i][j] + "人");
                            }
                        }
                        fwrite.write("\r\n");
                    }
                }
                fwrite.write("// 该文档并非真实数据，仅供测试使用");
                fwrite.close();
            } catch (Exception ioe) {
                ioe.printStackTrace();
            }

        }

        //idea输出文本
        public void show() {
            try {
                if(is_province[0]==0)
                    is_province[0]=1;
                for (int i = 0; i < province.length; i++) {
                    if (is_province[i] == 1) {
                        System.out.print(province[i] + " ");
                        for (int j = 0; j < type.length; j++) {
                            if (in_type[j] == 1) {
                                System.out.print(cntype[j] + " "+ person[i][j] + "人");
                            }
                        }
                        System.out.print("\n");
                    }
                }
                System.out.print("// 该文档并非真实数据，仅供测试使用");
            } catch (Exception ioe) {
                ioe.printStackTrace();
            }

        }
    }

    public static void main(String[] args) {
        String[] arg={"list","-log","C:\\Users\\61685\\IdeaProjects\\221701324\\log\\","-date","2020-01-23","-out","C:\\Users\\61685\\IdeaProjects\\221701324\\result\\listout1.txt"};
        InfectStatistic tt = new InfectStatistic();
        InfectStatistic.Command command = tt.new Command(arg);
        boolean b = command.isrt_cmd();

        InfectStatistic.File_handle filehandle = tt.new File_handle();
        filehandle.get_Flist();
        filehandle.Write_txt();
        filehandle.show();
    }



}
