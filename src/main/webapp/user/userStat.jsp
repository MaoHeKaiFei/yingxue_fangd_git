<%@page pageEncoding="UTF-8" isELIgnored="false"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<%--引入echarts--%>
<script src="${path}/js/echarts.min.js"></script>
<%--引入goeasy--%>
<script type="text/javascript" src="https://cdn.goeasy.io/goeasy-1.0.5.js"></script>

<!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
<div align="center">
    <div id="main" style="width: 1000px;height:600px;"></div>
</div>
<script type="text/javascript">

    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('main'));

    $(function () {
        $.post("${path}/eCharts/userBar",function (data) {
            // 指定图表的配置项和数据
            var option = {
                title: {
                    text: 'ECharts 入门示例'
                },
                tooltip: {},
                legend: {
                    data:['男生','女生'] //选项卡
                },
                xAxis: {
                    data:data.title //横坐标
                },
                yAxis: {},
                series: [{
                    name: '男生',
                    type: 'bar',
                    data: data.boys
                },{
                    name: '女生',
                    type: 'bar',
                    data: data.girls
                }]
            };

            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(option);

        },"json")


    })
</script>

<%--实时更新的user统计--%>
<script type="text/javascript">

    /*初始化GoEasy对象*/
    var goEasy = new GoEasy({
        host:'hangzhou.goeasy.io', //应用所在的区域地址: 【hangzhou.goeasy.io |singapore.goeasy.io】
        appkey:"BC-16892684edb14ba8bbbe075fb4290939", //替换为您的应用appkey
    });
    //GoEasy-OTP可以对appkey进行有效保护,详情请参考​ ​

    $(function(){
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('main'));


        //接收消息
        goEasy.subscribe({
            channel: "yingxue", //替换为您自己的channel
            onMessage: function (message) {
                //获取GoEasy数据
                var datas=message.content;

                //将json字符串转为javascript对象
                var data=JSON.parse(datas);

                // 指定图表的配置项和数据
                var option = {
                    title: {
                        text: '用户月注册统计',  //标题
                        subtext:"纯属虚构",
                        link:"${path}/main/main.jsp",
                        target:"self"
                    },
                    tooltip: {},  //鼠标提示
                    legend: {
                        data:['小男孩','小姑娘']  //选项卡
                    },
                    xAxis: {
                        data: data.month
                    },
                    yAxis: {},  //自适应
                    series: [{
                        name: '小男孩',
                        type: 'line',
                        data: data.boys
                    },{
                        name: '小姑娘',
                        type: 'bar',
                        data: data.girls
                    }]
                };

                // 使用刚指定的配置项和数据显示图表。
                myChart.setOption(option);
            }
        });
    });

</script>

