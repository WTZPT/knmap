
function doSave() {
    var data = JSON.stringify($('#spiderinfo_form').serializeJSON());
    $.ajax({
        type: "POST",
        url:"/spiderinfo",
        data:data,
        contentType: "application/json;charset=UTF-8",
        success: function(data) {
            if(data){
                window.location.reload()
            }
        }
    })
    window.location.reload()
}

function doPreview(title,preUrl,area,match,matchType){
    var previewUrl = $("input[name="+preUrl+"]").val();
    var previewMatch =  $("input[name="+match+"]").val();
    var dynamicSite = $("input[name='dynamicSite']").val();
    var url = "/spiderinfo/preview";
    var data = {
        "url":previewUrl,
        "area":area,
        "match":previewMatch,
        "matchType":matchType,
        "dynamicSite":dynamicSite
    };
    if(previewUrl == null) {
        swal("请输入预览URL地址！");
    } else if(previewMatch == null) {
        swal("请输入"+title+"!");
    } else{
        $.ajax({
            type: "POST",
            url: url,
            data:JSON.stringify(data),
            contentType: "application/json",
            success: function(data) {
                if(data){
                    var previewArea = document.getElementById("previewArea");
                    var html = '<textarea>'+data["result"].content+'</textarea>';
                    previewArea.innerHTML = html
                }
            }
        })
    }
}

$.fn.serializeObject = function() {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [ o[this.name] ];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};
