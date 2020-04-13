<%@page pageEncoding="UTF-8" isELIgnored="false"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>


<script src="${path}/js/echarts.min.js"></script>
<script type="text/javascript" src="${path}/js/china.js"></script>
<script type="text/javascript" src="${path}/bootstrap/js/jquery.min.js"></script>

<script type="text/javascript">
    $(function(){

        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('main'));


        $.post("${path}/eCharts/userMap",function (data) {



            var option = {
                title : {
                    text: '每月用户注册量',
                    subtext: '纯属虚构',
                    left: 'center'
                },
                tooltip : {
                    trigger: 'item'
                },
                legend: {
                    orient: 'vertical',
                    left: 'left',
                    data:['男生','女生']
                },
                visualMap: {
                    min: 0,
                    max: 200,
                    left: 'left',
                    top: 'bottom',
                    text:['高','低'],           // 文本，默认为数值文本
                    calculable : true
                },
                toolbox: {
                    show: true,
                    orient : 'vertical',
                    left: 'right',
                    top: 'center',
                    feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                series : [
                    {
                        name: '男生',
                        type: 'map',
                        mapType: 'china',
                        roam: false,
                        label: {
                            normal: {
                                show: false
                            },
                            emphasis: {
                                show: true
                            }
                        },
                        data:data.boy
                    },
                    {
                        name: '女生',
                        type: 'map',
                        mapType: 'china',
                        label: {
                            normal: {
                                show: false
                            },
                            emphasis: {
                                show: true
                            }
                        },
                        data:data.girl
                    },

                ]
            };

            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(option);




        },"json")



    });
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


                var option = {
                    title : {
                        text: '每月用户注册量',
                        subtext: '纯属虚构',
                        left: 'center'
                    },
                    tooltip : {
                        trigger: 'item'
                    },
                    legend: {
                        orient: 'vertical',
                        left: 'left',
                        data:['男生','女生']
                    },
                    visualMap: {
                        min: 0,
                        max: 200,
                        left: 'left',
                        top: 'bottom',
                        text:['高','低'],           // 文本，默认为数值文本
                        calculable : true
                    },
                    toolbox: {
                        show: true,
                        orient : 'vertical',
                        left: 'right',
                        top: 'center',
                        feature : {
                            mark : {show: true},
                            dataView : {show: true, readOnly: false},
                            restore : {show: true},
                            saveAsImage : {show: true}
                        }
                    },
                    series : [
                        {
                            name: '男生',
                            type: 'map',
                            mapType: 'china',
                            roam: false,
                            label: {
                                normal: {
                                    show: false
                                },
                                emphasis: {
                                    show: true
                                }
                            },
                            data:data.boy
                        },
                        {
                            name: '女生',
                            type: 'map',
                            mapType: 'china',
                            label: {
                                normal: {
                                    show: false
                                },
                                emphasis: {
                                    show: true
                                }
                            },
                            data:data.girl
                        },

                    ]
                };

                // 使用刚指定的配置项和数据显示图表。
                myChart.setOption(option);
            }
        });
    });

</script>





<!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
<div align="center">
    <div id="main" style="width: 1000px;height:600px;"></div>
</div>