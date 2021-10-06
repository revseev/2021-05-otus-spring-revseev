$(document).ready(function () {
    getTable();
})

function getTable() {
    $.ajax({
        type: 'GET',
        url: "/api/v1/books/",
        contentType: 'application/json;',
        dataType: 'JSON',
        success: function (books) {
            $("#bookTable").empty();

            for (let i in books) {
                $("#bookTable").append(
                    "<tr> \
                        <td>" + books[i].title + "</td> \
                        <td>" + books[i].authorName + "</td> \
                        <td>" + books[i].genres + "</td> \
                        <td>\
                            <a class='comments' href='/book/comments?id=" + books[i].id + "'>Comments</a>&nbsp \
                            <a class='edit' href='/book/edit?id=" + books[i].id + "'>Edit</a>&nbsp \
                            <a class='delete' href='/book/delete?id=" + books[i].id + "'>Delete</a> \
                        </td> \
                    </tr>")
            }
        }
    })
}