'use strict';

/* Directives */

angular.module('Game.directives', ['ngResource', 'ngRoute', 'ngAnimate', 'ngCookies', 'ui.bootstrap', 'toaster', 'pascalprecht.translate']).directive('appVersion', ['version',
function(version) {
	return function(scope, elm, attrs) {
		elm.text(version);
	};
}]).directive('deleteDialog', function() {
	return {
		transclude : true,
		scope : {
			title : '=',
			message : '=',
			deleteFn : '&'
		},
		templateUrl : 'templates/test-dialog.html'
	};
}).directive('userInfo', ['jQuery',
function($) {

	return {
		restrict : 'E',
		transclude : true,
		scope : {
		},
		link : function(scope, element, attrs, ctrl, transclude, formCtrl) {
			$.cssEase['bounce'] = 'cubic-bezier(0,1,0.5,1.3)';
			$(element).find("#div_user_icon").transition({
				left : '40px'
			}, 1000, 'ease');
			$(element).find("#div_apple_in_stock_info").transition({
				top : '-20px'
			}, 1000, 'bounce');
			$(element).find("#div_money_in_stock_info").transition({
				top : '-20px'
			}, 1000, 'bounce');
			$(element).find("#div_eco_points_info").transition({
				top : '-20px'
			}, 1000, 'bounce');
		},
		templateUrl : '/public/templates/user-info.html'
	};

}]).directive('shopMenu', ['jQuery',
function($) {

	return {
		restrict : 'E',
		transclude : true,
		scope : {},
		link : function(scope, element, attrs, ctrl, transclude, formCtrl) {
			$.cssEase['bounce'] = 'cubic-bezier(0,1,0.5,1.3)';
			scope.showNext = true;
			scope.visible = false;
			scope.buying = false;
			scope.itemClick = function(item) {
				if (!scope.buying) {
					scope.buying = true;
					scope.$root.$emit('buy-item', item);
				}
			};

			scope.$root.$on("item-bought", function($scope) {
				scope.buying = false;
			});

			scope.$root.$on("shop-show", function($scope, cfg) {
				scope.items = cfg.items;
				scope.storeUrl = cfg.storeUrl;
				if (cfg.showNext !== null) {
					scope.showNext = cfg.showNext;
				}
				scope.visible = true;
				show();
			});

			scope.$root.$on("shop-hide", function($scope) {
				scope.visible = false;
				hide();
			});

			function hide() {
				$(element).find("#div-shop-manu").transition({
					right : '-750px'
				}, 700, 'ease');
				scope.visible = false;
			}

			function show() {
				$(element).find("#div-shop-manu").transition({
					right : '350px'
				}, 700, 'ease');
				scope.visible = true;
			}


			$(element).find("#shop_arrow_main_menu").bind('click', hide);

		},
		templateUrl : '/public/templates/shop-menu.html'
	};

}]).directive('weatherInfoDetails', ['jQuery',
function($) {
	return {
		restrict : 'E',
		transclude : true,
		scope : {
			w : '='
		},
		link : function(scope, element, attrs, ctrl, transclude, formCtrl) {
			function hide() {
				// $(element).find("#weather-info-details").transition({
				// right : '-750px'
				// }, 700, 'ease');
				$(element).find("#weather-info-details").animate({
					opacity : 0
				});
			}

			function show(position) {
				// $(element).find("#weather-info-details").transition({
					// right : '350px'
				// }, 700, 'ease');
				$(element).find("#weather-info-details").css("top",position.top);
				$(element).find("#weather-info-details").css("left",position.left);
				$(element).find("#weather-info-details").animate({
					opacity : 1
				});
			}


			scope.$root.$on("weather-hide", function($scope) {
				hide();
			});
			scope.$root.$on("weather-show", function($scope, w, right) {
				scope.w = w;
				show(right);
			});

		},
		templateUrl : '/public/templates/weather-info-details.html'
	};
}]).directive('weatherInfo', ['jQuery',
function($) {
	return {
		restrict : 'E',
		transclude : true,
		scope : {
			weather : '=',
			detailWeather : '=',
		},

		link : function(scope, element, attrs, ctrl, transclude, formCtrl) {

			scope.$root.$emit("weather-hide");
			scope.visible = false;
			scope.detailsWeather = function(w) {
				scope.toggle(w);
				//scope.$root.$emit("weather-show", w);
			};
			scope.toggle = function(w) {
				if (scope.visible == false) {
					$(element).find(".weather-details-info-more-less").css("visibility", "hidden");
					$(element).find("#weather-details-info-more-less-" + w.id).css("visibility", "visible");
					$(element).find("#weather-details-info-more-less-span-" + w.id).text("-");
					$(element).find("#weather-details-info-more-less-" + w.id).css("background-color","cyan");
					var position = $(element).find("#weather-details-info-more-less-" + w.id).position();
					position.top = position.top + $('.weather_wrapper').position().top + $(element).find("#weather-details-info-more-less-" + w.id).height()+5;
					position.left = $('.weather_wrapper').position().left + position.left;
					scope.$root.$emit("weather-show", w, position);
					scope.visible = true;
				} else {
					$(element).find(".weather-details-info-more-less").css("visibility", "visible");
					$(element).find(".weather-details-info-more-less-span").text("+");
					$(element).find("#weather-details-info-more-less-" + w.id).css("background-color","white");
					scope.$root.$emit("weather-hide");
					scope.visible = false;
				}
			};
			//scope.$root.$emit('detailsWeather', w);
		},
		templateUrl : '/public/templates/weather-info.html'
	};
}]).directive('sideMenu', ['jQuery',
function($) {

	return {
		restrict : 'E',
		transclude : true,
		scope : {
			weather : '=',
			actions : '='
		},
		link : function(scope, element, attrs, ctrl, transclude, formCtrl) {
			scope.itemClick = function(a) {
				a.action();
			};

			scope.visible = false;
			scope.$root.$on("shop-show", function(data) {
				if (scope.visible) {
					hide();
				}
			});

			function show() {
				$(element).find("#div-side-screen").transition({
					right : '-10px'
				}, 700, 'ease');
				$(element).find("#home_arrow_main_menu").transition({
					rotate : '0deg'
				});
				scope.$root.$emit("side-show");
				scope.visible = true;
			}

			function hide() {
				$(element).find("#div-side-screen").transition({
					right : '-570px'
				}, 700, 'ease');
				$(element).find("#home_arrow_main_menu").transition({
					rotate : '180deg'
				});
				scope.$root.$emit("side-hide");
				scope.visible = false;
			}

			function onClick() {
				if (scope.visible) {
					hide();
				} else {
					show();
				}
			}


			$(element).find("#home_arrow_main_menu").bind('click', onClick);

		},
		templateUrl : '/public/templates/side-menu.html'
	};

}]);
