$(function() {
	$("#page").append('<script src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.js"></script>');
	const $head = $("head")[0];
	$('<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>').appendTo($head)
	$('<script src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.js"></script>').appendTo($head);
	$('<script src="//cdn.jsdelivr.net/npm/sweetalert2@11"></script>').appendTo($head);
	$('<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">').appendTo($head);
	$('<link rel="stylesheet" href="/css/main.css">').appendTo($head);
	$('<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.css">').appendTo($head);
});