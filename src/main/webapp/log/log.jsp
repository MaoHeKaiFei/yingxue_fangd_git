<%@page pageEncoding="UTF-8" isELIgnored="false"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<script>
    $(function () {
        $("#logTable").jqGrid({
            url:"${path}/log/selectByPage",
            styleUI:'Bootstrap',//使用bootstrap风格的样式
            datatype : "json",
            pager:"#logPage",
            rowNum:4,
            rowList:[2,4,6],//用来指定下拉列表中每页显示的条数
            viewrecords:true,//是否显示总记录数
            height:"auto",
            autowidth:true,
            editurl:"${path}/log/edit",//指定编 multiselect:true,//是否显示checkbox辑(增删改)时的url

            colNames:["ID","管理员","时间","操作","状态"],
            colModel:[                       //数据
                {name:"id"},//列对应json属性
                {name:"adminname",editable:true},
                {name:"date",editable:true},
                {name:"operation", editable:true},//对当前列进行二次渲染
                {name:"status",editable:true}
            ]
        })



        //表单工具栏
        $("#logTable").jqGrid("navGrid","#logPage",
            {edit : true,add : true,del : true,addtext:"添加",edittext:"修改",deltext:"删除"},

            {closeAfterEdit:true}, //修改完成关闭模态框

            {closeAfterAdd:true, }, //添加 //关闭对话框
            {}//删除
        )


    });//懒加载结束
</script>

<%--初始化一个面板头--%>
<div class="panel panel-info">
    <%--面板头--%>
    <div class="panel panel-heading">
        <h2>日志信息</h2>
    </div>

    <%--标签页--%>
    <div class="nav nav-tabs">
        <li class="active"><a href="">日志信息</a></li>
    </div>

    <%--按钮--%>
    <button class="btn btn-info">导出日志信息</button>

    <%--初始化表单--%>
    <table id="logTable" />

    <%--分页工具栏--%>
    <div id="logPage" />
</div>