<%@page pageEncoding="UTF-8" isELIgnored="false"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<script>
    $(function () {
        $("#videoTable").jqGrid({
            url:"${path}/video/selectByPage",
            styleUI:'Bootstrap',//使用bootstrap风格的样式
            datatype : "json",
            pager:"#videoPage",
            rowNum:4,
            rowList:[2,4,6],//用来指定下拉列表中每页显示的条数
            viewrecords:true,//是否显示总记录数
            height:"auto",
            autowidth:true,
            editurl:"${path}/video/edit",//指定编 multiselect:true,//是否显示checkbox辑(增删改)时的url

            colNames:["ID","名称","视频地址","上传时间","描述","所属类别","类别id","用户id"],
            colModel:[                       //数据
                {name:"id"},//列对应json属性
                {name:"title",editable:true},
                {name:"path", editable:true,align:"center",edittype:"file",
                    formatter:function (cellvalue, options, rowObject) {
                        return "<video src='"+rowObject.path+"' width='100px' height='100px' controls />";
                    }},
                {name:"publishDate"},
                {name:"brief",editable:true},
                {name:"categoryId"},
                {name:"categoryId"},
                {name:"userId"}
            ]
        })



        //表单工具栏
        $("#videoTable").jqGrid("navGrid","#videoPage",
            {edit : true,add : true,del : true,addtext:"添加",edittext:"修改",deltext:"删除"},

            {closeAfterEdit:true,
                beforeShowForm :function(obj){
                    obj.find("#path").attr("disabled",true);//禁用input
                }
            }, //修改完成关闭模态框

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
                        url:"${path}/video/uploadVideo",
                        type:"post",
                        //dataType:"json",
                        fileElementId:"path",    //上传的文件域的ID
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
            {
                afterSubmit:function(data){
                    //向警告框中追加错误信息
                    $("#showMsg").html(data.responseJSON.message);
                    //展示警告框
                    $("#deleteMsg").show();

                    //自动关闭
                    setTimeout(function(){
                        $("#deleteMsg").hide();
                    },3000);
                    return "hello";
                }
            }//删除
        )



    });//懒加载结束

</script>

<%--初始化一个面板头--%>
<div class="panel panel-info">
    <%--面板头--%>
    <div class="panel panel-heading">
        <h2>视频信息</h2>
    </div>

    <%--标签页--%>
    <div class="nav nav-tabs">
        <li class="active"><a href="">视频信息</a></li>
    </div>

    <%--警告框--%>
    <div id="deleteMsg" class="alert alert-warning alert-dismissible"  role="alert" style="width: 300px;display: none">
        <span id="showMsg" />
    </div>

    <%--按钮--%>
    <button class="btn btn-info">导出视频信息</button>

    <%--初始化表单--%>
    <table id="videoTable" />

    <%--分页工具栏--%>
    <div id="videoPage" />
</div>