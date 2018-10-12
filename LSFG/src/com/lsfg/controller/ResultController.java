package com.lsfg.controller;

import com.lsfg.base.OracleConnection;
import com.lsfg.model.Custom;
import com.lsfg.model.ResultModel;
import com.lsfg.model.Status;
import com.lsfg.util.JsonUtils;
import com.lsfg.util.RegexUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.net.*;
import java.io.UnsupportedEncodingException;
/**
 * Created by lpeng on 2018/03/31.
 */

@Controller
@RequestMapping("/result/*")
public class ResultController {

    private static Logger logger = Logger.getLogger(ResultController.class);

    /**
     * 进入区域列表.
     *
     * @return
     */
    @RequestMapping(value = "toGetResult.do", method = RequestMethod.GET, produces={"application/json;charset=UTF-8"})
    public @ResponseBody String toGetResult(@RequestParam("zjhm") String zjhm ,@RequestParam("htbh") String htbh){
        ResultModel resultModel = new ResultModel();
        List<Custom> customList = new ArrayList<>();
        Status status = new Status();

        Connection connection = null;
        Statement stm = null;
        ResultSet rs = null;
        if (true) {
            try {
                connection = OracleConnection.getConnection();
                if (connection != null && !connection.isClosed()) {
                    stm = connection.createStatement();
                    StringBuilder sql = new StringBuilder();
                    sql.append(" SELECT * ");
                    sql.append(" FROM  (SELECT gfrmc,zjhm,htbh,fwzl,jzmj,ghyt,htbasj,htje,qymc ");
                    sql.append(" FROM T_SPFWJYXX ) ");
                    sql.append(" WHERE zjhm = '").append(zjhm).append("'");
                    sql.append(" AND htbh = '").append(htbh).append("'");
                    rs = stm.executeQuery(sql.toString());
                    while (rs.next()){
                        Custom custom = new Custom();
                        custom.setgfrmc(rs.getString(1));
                        custom.setzjhm(rs.getString(2));
                        custom.sethtbh(rs.getString(3));
                        custom.setfwzl(rs.getString(4));
                        custom.setjzmj(rs.getString(5));
                        custom.setghyt(rs.getString(6));
                        custom.sethtbasj(rs.getTimestamp(7));
                        custom.sethtje(rs.getFloat(8));
                        custom.setqymc(rs.getString(9));
                        customList.add(custom);
                    }
                    status.setCode(1);
                    status.setText("操作成功");
                } else {
                    status.setCode(0);
                    status.setText("数据库连接异常，请稍后再查询");
                }
            } catch (Exception e) {
                e.printStackTrace();
                //

            } finally {
                try {
                    OracleConnection.release(stm, connection);
                } catch (SQLException e) {
                    e.printStackTrace();
                    logger.error("数据库关闭连接异常：" + e.getMessage());
                    status.setCode(0);
                    status.setText("数据库关闭连接异常");
                }
            }
        } else {
            status.setCode(0);
            status.setText("输入的号码有误，请重新输入");
        }

        resultModel.setCustomList(customList);
        resultModel.setStatus(status);
        //转为json格式
        String result = JsonUtils.obj2StringPretty(resultModel);
        return result;



    }

}




