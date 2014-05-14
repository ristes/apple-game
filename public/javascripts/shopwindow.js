$(document).ready(function() {
	$.cssEase['bounce'] = 'cubic-bezier(0,1,0.5,1.3)';
	$("#div_user_icon").transition({
		left : '40px'
	}, 700, 'ease');
	$("#div_apple_in_stock_info").transition({
		top : '-20px'
	}, 1000, 'bounce');
	$("#div_money_in_stock_info").transition({
		top : '-20px'
	}, 1000, 'bounce');
	$("#div_eco_points_info").transition({
		top : '-20px'
	}, 1000, 'bounce');
	$("#shop_arrow_main_menu").bind('click', function() {
		$("#div-shop-manu").transition({
			right : '-750px'
		}, 700, 'ease');
		$("#div-shop-manu").visibility("hidden");
	});
	$("#home_arrow_main_menu").toggle(function() {

		$("#div-home-screen").transition({
			right : '-10px'
		}, 700, 'ease');
		$("#home_arrow_main_menu").transition({
			rotate : '0deg'
		});
		$("#shop_arrow_main_menu").trigger("click");
	}, function() {
		$("#div-home-screen").transition({
			right : '-570px'
		}, 700, 'ease');
		$("#home_arrow_main_menu").transition({
			rotate : '180deg'
		});
	});
	$("#prodavnica_home_icon").bind("click", function() {
		$("#home_arrow_main_menu").trigger("click");
		$("#div-shop-manu").transition({
			right : '350px'
		}, 700, 'ease');
	});
});
