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
                let genresString = books[i].genres.map(function (val, index) {
                    return val.name
                }).join(", ");

                $("#bookTable").append(
                    "<tr> \
                        <td>" + books[i].title + "</td> \
                        <td>" + books[i].authorName + "</td> \
                        <td>" + genresString + "</td> \
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