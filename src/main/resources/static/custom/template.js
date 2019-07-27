
function ShowCreateModal(){

    $('#createFileMModal').modal('show');
}


function createplateClass() {
    var data = $('#fieldName').val();
    console.log(data)
    $.ajax({
        type: "POST",
        url:"/template/createTemplateClass?fieldName="+data,
        contentType: "application/json;charset=UTF-8",
        success: function(data) {
            if(data){
                alert(data.msg)

            }
        }
    })
}


function ShowUpdateModal(id) {
    var nameId = "#name"+id;
    console.log(nameId)
    console.log($(nameId).text())
    $('#className').val($(nameId).text())
    $('#classId').val(id);
    $('#updateFileModal').modal('show');
}


function UpdatelateClass() {

    var data = {}
     data["fieldName"] = $('#updatefieldName').val();
     data["id"] = $('#classId').val();

    console.log(JSON.stringify(data));
    if(data["fieldName"] == null) {
        alert("领域名不能为空！")
    } else {
        $.ajax({
            type: "POST",
            url:"/template/updateTemplateClass",
            data:JSON.stringify(data),
            contentType: "application/json;charset=UTF-8",
            success: function(data) {
                if(data){
                    alert(data.msg)
                }
            }
        })
    }
}



