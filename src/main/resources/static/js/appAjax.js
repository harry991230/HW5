


function ajaxRequest(uri, data) {
    return $.ajax({
        url: uri,
        data: JSON.stringify(data),
        method: 'POST',
        contentType: "application/json",
    });
}