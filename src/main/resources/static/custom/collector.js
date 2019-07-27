
function startSpider(id) {

    $.ajax({
        type: "POST",
        url:"/spiderinfo/start?id="+id,
        contentType: "application/json;charset=UTF-8",
        success: function(data) {
            if(data){
                console.log(data.toString())
                console.log(data.msg)
                alert(data.msg)

                // window.location.reload()
            }
        }
    })

}

function prompt(newName, time, fn) {
    var $div = $('<div></div>');
    $div.css({
        'position': 'fixed',
        'top': 0,
        'left': 0,
        'width': '100%',
        'height': '100%',
        'z-index': '200',
        'background-color': 'rgba(0,0,0,0.4)',
        // 'background-color':'#000',
    });
    var $contentDiv = $('<div>' + newName + '</div>');
    $contentDiv.css({
        'position': 'absolute',
        'top': '50%',
        'left': '50%',
        'font-size': '25px',
        'padding': '50px 100px',
        'border-radius': '5px',
        'background-color': '#fff',
        'color': '#000'
    });
    $div.append($contentDiv);
    $('body').append($div);

    // 获取创建的大小
    var newW = (parseInt($contentDiv.css('width')) + 200) / 2;
    var newH = (parseInt($contentDiv.css('height')) + 100) / 2;
    $contentDiv.css({
        'margin-top': -newH + 'px',
        'margin-left': -newW + 'px',
    })
    setTimeout(function() {
        $div.remove();
        if (fn) {
            fn(); //回调函数
        }

    }, time);
}