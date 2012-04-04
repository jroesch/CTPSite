function deletePost(id) {
    if(confirm("Are you sure you would like to delete this post?")) {
        $.ajax({
           url: "/posts/" + id,
           type: "DELETE",
           success: function (data) { location.reload() }
        });
    }
}