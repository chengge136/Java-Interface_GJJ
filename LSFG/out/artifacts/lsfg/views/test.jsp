<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>信息查询接口</title>
    <base href="<%=basePath%>">
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <!-- basic styles -->
    <link href="assets/css/bootstrap.min.css" rel="stylesheet" />
    <link rel="stylesheet" href="assets/css/font-awesome.min.css" />
    <link rel="stylesheet" href="assets/css/ace.min.css" />
    <link rel="stylesheet" href="assets/css/ace-rtl.min.css" />
    <link rel="stylesheet" href="assets/css/ace-skins.min.css" />
    <link rel="stylesheet" href="assets/css/AdminLTE.css" />

    <script type="text/javascript" src="assets/js/jquery-2.0.3.min.js"></script>
    <script src="assets/js/typeahead-bs2.min.js"></script>
    <script src="assets/js/ace-elements.min.js"></script>
    <script src="assets/js/ace.min.js"></script>
    <script src="assets/js/ace-extra.min.js"></script>

    <script type="text/javascript">
        function getResult(){
            var zjhm = $("#zjhm").val();
            var htbh = $("#htbh").val();
            if(zjhm !== '' && zjhm != null) {
                if (htbh !== '' && htbh != null) {
                    $.ajax({
                        //提交方式
                        type: "get",
                        //action
                        url: "result/toGetResult.do?zjhm=" + zjhm + "&htbh=" + htbh,
                        //数据类型
                        dataTyep: 'json',
                        contentType: 'application/json; charset=utf-8',//设置编码格式
                        //同步
                        success: function (data) {
                            $("#result").text('');
                            $("#result").append("查询结果：" + JSON.stringify(data));
                        }
                    });
                    //返回false，否则表单会自己再做一次提交操作，并且页面跳转
                    return false;
                }
            }
        }
    </script>
</head>
<body>
<div class="main-container" id="main-container">
    <div class="main-content">
        <div class="row">
            <div class="col-xs-12 col-sm-6 col-md-3">
                <div style='width:350px;'>
                    <div style='width:320px;margin-left: 3%;' class='form-group has-feedback'>
                        <label for='zjhm'>请输入身份证号码</label>
                        <input type='text' id='zjhm' name='zjhm' class='form-control' />
                        <label for='htbh'>请输入合同编号</label>
                        <input type='text' id='htbh' name='htbh' class='form-control' />
                    </div>
                    <button class='btn btn-block btn-primary btn-lg' id="search" onclick='getResult()'>查 询</button>
                </div>
            </div>
        </div>
    </div>
</div>

<h3 id="result"></h3>

</body>
</html>
