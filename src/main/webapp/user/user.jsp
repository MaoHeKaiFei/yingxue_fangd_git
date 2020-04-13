<%@page pageEncoding="UTF-8" isELIgnored="false"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<script>
        $(function () {
            $("#userTable").jqGrid({
                url:"${path}/user/selectByPage",
                styleUI:'Bootstrap',//使用bootstrap风格的样式
                datatype : "json",
                pager:"#userPage",
                rowNum:4,
                rowList:[2,4,6],//用来指定下拉列表中每页显示的条数
                viewrecords:true,//是否显示总记录数
                height:"auto",
                autowidth:true,
                editurl:"${path}/user/edit",//指定编 multiselect:true,//是否显示checkbox辑(增删改)时的url

                colNames:["ID","用户名","手机号","头像","简介","微信","状态","注册时间","城市","性别"],
                colModel:[                       //数据
                    {name:"id"},//列对应json属性
                    {name:"username",editable:true},
                    {name:"phone",editable:true},
                    {name:"headImg", editable:true,align:"center",edittype:"file",//对当前列进行二次渲染
                        //cellvalue当前列的值, options当前的属性对象(操作), rowObject当前行的行对象(整行的数据)
                        formatter:function (cellvalue, options, rowObject) {
                            return "<img src='${path}/upload/photo/"+cellvalue+"' width='100px' height='100px' />";
                        }},
                    {name:"sign",editable:true},
                    {name:"wechat",editable:true},
                    {name:"status",formatter:function (cellvalue, options, rowObject) {
                            if(cellvalue==0){//1.激活 2.冻结
                                return "<button  onclick='statusUpdate(\""+rowObject.id+"\",\""+rowObject.status+"\")' class='btn btn-info'>冻结</button>";
                            }else{
                                return "<button  onclick='statusUpdate(\""+rowObject.id+"\",\""+rowObject.status+"\")' class='btn btn-warning'>激活</button>";
                            }
                        }},
                    {name:"createDate"},
                    {name:"city",editable:true},
                    {name:"sex",editable:true}
                ]
            })



            //表单工具栏
            $("#userTable").jqGrid("navGrid","#userPage",
                {edit : true,add : true,del : true,addtext:"添加",edittext:"修改",deltext:"删除"},

                {closeAfterEdit:true}, //修改完成关闭模态框

                {
                    closeAfterAdd:true,  //关闭对话框
                    //文件上传
                    afterSubmit:function(response){
                        //数据的入库
                        //文件没有上传
                        // 图片路径不对
                        //在提交之后做文件上传   本地
                        //fileElementId　　　　　  需要上传的文件域的ID，即<input type="file">的ID。
                        //异步文件上传
                        $.ajaxFileUpload({
                            url:"${path}/user/uploadUser",
                            type:"post",
                            //dataType:"json",
                            fileElementId:"headImg",    //上传的文件域的ID
                            data:{id:response.responseText},
                            success:function(){
                                //刷新表单
                                $("#userTable").trigger("reloadGrid");
                            }
                        });
                        //必须有返回值
                        return "aaa";
                    }

                 }, //添加
                {}//删除
            )



        });//懒加载结束

        function statusUpdate(id,status){
            //根据id修改状态 id status
            if(status==1){
                $.ajax({
                    url:"${path}/user/edit",
                    data:{id:id,status:"0","oper":"edit"},
                    type:"post",
                    dataType:"Text",
                    success:function () {
                        //刷新表单
                        $("#userTable").trigger("reloadGrid");
                    }
                })
            }else {
                $.ajax({
                    url:"${path}/user/edit",
                    data:{id:id,status:"1","oper":"edit"},
                    type:"post",
                   dataType:"Text",
                    success:function () {
                        //刷新表单
                        $("#userTable").trigger("reloadGrid");
                    }
                })
            }
        }


        $("#output").click(function () {
            $.post("${path}/user/output",function () {},"json");
        })
</script>

<%--初始化一个面板头--%>
<div class="panel panel-info">
    <%--面板头--%>
    <div class="panel panel-heading">
        <h2>用户信息</h2>
    </div>

    <%--标签页--%>
    <div class="nav nav-tabs">
        <li class="active"><a href="">用户信息</a></li>
    </div>

    <%--按钮--%>
    <button id="output" class="btn btn-info">导出用户信息</button>

    <%--初始化表单--%>
    <table id="userTable" />

    <%--分页工具栏--%>
    <div id="userPage" />
</div>