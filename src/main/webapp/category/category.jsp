<%@page pageEncoding="UTF-8" isELIgnored="false"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>

<script>
    /*延迟加载*/
    $(function(){
        //初始化表格的方法
        pageInit();
    });

    //初始化表格方法
    function pageInit(){

        //父表格
        $("#cateTable").jqGrid({
            url : "${path}/category/firstCate",
            datatype : "json",
            rowNum : 8,
            styleUI:"Bootstrap",
            height:"auto",
            autowidth:true,
            rowList : [ 8, 10, 20, 30 ],
            pager : '#catePage',
            viewrecords : true,
            subGrid : true,  //是否开启子表格
            editurl:"${path}/category/edit",//增删改)时的url

            colNames : [ 'Id', '类别名', '级别'],
            colModel : [
                {name : 'id',index : 'id',  width : 55},
                {name : 'cateName',index : 'invdate',width : 90,editable:true},
                {name : 'levels',index : 'name',width : 100,editable:true}
            ],
            //1.subgrid_id   点击一行时会在父表格中创建一个div，用来容纳子表格，subgrid_id就是div的id
            //2.row_id   点击行的id
            subGridRowExpanded : function(subgridId, rowId) {  //设置子表格相关的属性
                //复制子表格内容
                addSubGrid(subgridId, rowId);
            }
        });

        //父表格分页工具栏
        $("#cateTable").jqGrid('navGrid', '#catePage', {add : true,edit : true,del : true},
            {closeAfterEdit:true},//修改完成关闭模态框
            {closeAfterAdd:true },//添加 //关闭对话框
            {
                closeAfterDel:true,
                //将提示信息渲染到页面上
                afterSubmit:function (response) {
                    //将警告框内容放到
                    $("#showMeg").html(response.responseJSON.message);
                    $("#deleteMasg").show();

                    return "oo";
                }

            }//删除
        );
    }







    //子表格
    function addSubGrid(subgridId, rowId){
        var subgridTableId= subgridId+"Table";  //定义子表格 Table的id
        var pagerId= subgridId+"Page";   //定义子表格工具栏id

        //在子表格容器中创建子表格和子表格分页工具栏
        $("#" + subgridId).html("<table id='"+subgridTableId+"' /> <div id='"+pagerId+"'>");


        //子表格
        $("#" + subgridTableId).jqGrid({
            url:"${path}/category/twoCate?parentId="+rowId,
            datatype : "json",
            rowNum : 8,
            styleUI:"Bootstrap",
            height:"auto",
            autowidth:true,
            rowList : [ 8, 10, 20, 30 ],
            pager : "#"+pagerId,
            viewrecords : true,
            editurl:"${path}/category/edit?parentId="+rowId,

            colNames : [ 'Id', '类别名', '级别','上级类别id'],
            colModel : [
                {name : 'id',index : 'id',  width : 55},
                {name : 'cateName',index : 'invdate',width : 90,editable:true},
                {name : 'levels',index : 'name',width : 100,editable:true},
                {name : 'parentId',index : 'name',width : 100}
            ]
        });

        //子表格分页工具栏
        $("#" + subgridTableId).jqGrid('navGrid',"#" + pagerId, {edit : true,add : true,del : true},
            {closeAfterEdit:true},//修改完成关闭模态框
            {closeAfterAdd:true},//添加 //关闭对话框
            {
                closeAfterdel:true,  //删除后关闭对话框
                //提交之后的操作
                afterSubmit:function(response){
                    //向警告框中写入内容
                    $("#showMsg").html(response.responseJSON.message);
                    //展示警告框
                    $("#deleteMsg").show();

                    //自动关闭
                    setTimeout(function(){
                        //关闭提示框
                        $("#deleteMsg").hide();
                    },2000);

                    return "hello";
                }


            }//删除
        );
    }

</script>

<%--初始化一个面板--%>
<div class="panel panel-success">

    <%--面板头--%>
    <div class="panel panel-heading">
        <h2>类别信息</h2>
    </div>

    <%--标签页--%>
    <div class="nav nav-tabs">
        <li class="active"><a href="">类别信息</a></li>
    </div>

        <%--警告框--%>
        <div id="deleteMasg" class="alert alert-warning alert-dismissible" role="alert" style="width: 500px">
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
            <strong id="showMeg" />
        </div>

    <%--初始化表单--%>
    <table id="cateTable" />

    <%--分页工具栏--%>
    <div id="catePage" />

</div>