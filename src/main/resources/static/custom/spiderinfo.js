
function doSave() {
    var data = JSON.stringify($('#spiderinfo_form').serializeJSON());
    console.log(data)
    $.ajax({
        type: "POST",
        url:"/template/createTemplate",
        data:data,
        contentType: "application/json",
        success: function(data) {
            if(data){
                window.location.reload()
                console.log(data)
            }
        }
    })

}

var flag = 0;
function doAdd() {
    flag++;
    $("#change").append('<div class="form-group"><label class="col-md-2 control-label">SP0_SXpath</label><div class="col-md-5"><input name="spo[][articleSXpath]"type="text"class="form-control"value=""></div><div class="col-md-1"><button type="button"class="form-control"onclick="doPreview("SP0_SXpath预览","preViewArticleUrl","title","titleXpath","xpath")">预览</button></div></div><div class="form-group"><label class="col-md-2 control-label">SP0_PXpath</label><div class="col-md-5"><input name="spo[][articlePXpath]"type="text"class="form-control"value=""></div><div class="col-md-1"><button type="button"class="form-control"onclick="doPreview("SP0_PXpath预览","preViewArticleUrl","publishTime","timePostXpath","xpath")">预览</button></div></div><div class="form-group"><label class="col-md-2 control-label">SP0_OXpath</label><div class="col-md-5"><input name="spo[][articleOXpath]"type="text"class="form-control"value=""></div><div class="col-md-1"><button type="button"class="form-control"onclick="doPreview("SP0_OXpath预览","preViewArticleUrl","content","contentXpath","xpath")">预览</button></div></div>')
}

function doSub(){
    if($("#change>div").size() <= 3){
    } else{
        flag--;
        $("#change>div").last().remove();
        $("#change>div").last().remove();
        $("#change>div").last().remove();
    }
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
