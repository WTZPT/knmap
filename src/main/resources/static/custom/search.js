function search() {
    $.ajax({
        type: "GET",
        url: "/search/find",
        data: { fieldId: $("select").val(), key: $("#content").val() },
        dataType: "json",
        success: function (data) {
           // $('#main').empty();   //清空resText里面的所有内容
            //编辑echarts
            console.log(typeof(data));


            var person = data.data;

            var myChart = echarts.init(document.getElementById('main'));

            // 指定图表的配置项和数据
            var size = 60;
            var size1 = 30;
            var yy = 200;
            var yy1 = 250;

            var listdata = JSON.parse(person.nodeList) ;
            var links = JSON.parse(person.linkList) ;
            console.log(listdata)
            console.log(links)
            var med = "药品";
            var ope = "操作";
            var che = "检查";


            option = {
                title: {
                    text: "知识图谱",
                    top: "top",
                    left: "left",
                    textStyle: {
                        color: '#f7f7f7'
                    }
                },
                tooltip: {
                    formatter: function (x) {
                        return x.data.des;
                    }
                },
                series: [{
                    type: 'graph',
                    layout: 'force',
                    symbolSize: 100,
                    roam: true,
                    edgeSymbol: ['circle', 'arrow'],
                    edgeSymbolSize: [4, 10],
                    edgeLabel: {
                        normal: {
                            textStyle: {
                                fontSize: 20
                            }
                        }
                    },
                    force: {
                        repulsion: 10000,
                        edgeLength: [300, 400]
                    },
                    draggable: true,
                    itemStyle: {
                        normal: {
                            color: '#4b565b'
                        }
                    },
                    lineStyle: {
                        normal: {
                            width: 2,
                            color: '#4b565b'

                        }
                    },
                    edgeLabel: {
                        normal: {
                            show: true,
                            formatter: function (x) {
                                return x.data.name;
                            }
                        }
                    },
                    label: {
                        normal: {
                            show: true,
                            textStyle: {
                            }
                        }
                    },
                    data: listdata,
                    links: links
                    // categories: texts,

                }]
            };
            console.log("oooooo");
            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(option);

        }
    });
}

