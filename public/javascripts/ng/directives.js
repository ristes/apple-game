'use strict';

/* Directives */

angular.module(
        'Game.directives',
        ['ngResource', 'ngRoute', 'ngAnimate', 'ngCookies', 'ui.bootstrap',
            'toaster', 'pascalprecht.translate']).directive('appVersion',
        ['version', function(version) {
          return function(scope, elm, attrs) {
            elm.text(version);
          };
        }]).directive('deleteDialog', function() {
  return {
    transclude: true,
    scope: {
      title: '=',
      message: '=',
      deleteFn: '&'
    },
    templateUrl: 'templates/delete-dialog.html'
  };
}).directive('userInfo', ['jQuery', function($) {

  return {
    restrict: 'E',
    transclude: true,
    scope: {
      farmer: '='
    },
    link: function(scope, element, attrs, ctrl, transclude, formCtrl) {
      $.cssEase['bounce'] = 'cubic-bezier(0,1,0.5,1.3)';
      $(element).find("#div_user_icon").transition({
        left: '40px'
      }, 1000, 'ease');
      $(element).find("#div_apple_in_stock_info").transition({
        top: '-20px'
      }, 1000, 'bounce');
      $(element).find("#div_money_in_stock_info").transition({
        top: '-20px'
      }, 1000, 'bounce');
      $(element).find("#div_eco_points_info").transition({
        top: '-20px'
      }, 1000, 'bounce');
    },
    templateUrl: '/public/templates/user-info.html'
  };

}]).directive('shopMenu', ['jQuery', function($) {

  return {
    restrict: 'E',
    transclude: true,
    link: function(scope, element, attrs, ctrl, transclude, formCtrl) {
      $.cssEase['bounce'] = 'cubic-bezier(0,1,0.5,1.3)';

      scope.visible = false;

      scope.$root.$on("side-show", function() {
        if (scope.visible) {
          scope.visible = false;
          hide();
        }
      });

      scope.$root.$on("shop-show", function(data) {
        scope.data = data;
        scope.visible = true;
        show();
      });

      function hide() {
        $(element).find("#div-shop-manu").transition({
          right: '-750px'
        }, 700, 'ease');
        scope.visible = false;
      }

      function show() {
        $(element).find("#div-shop-manu").transition({
          right: '350px'
        }, 700, 'ease');
        scope.visible = true;
      }

      $(element).find("#shop_arrow_main_menu").bind('click', hide);

    },
    templateUrl: '/public/templates/shop-menu.html'
  };

}]).directive('sideMenu', ['jQuery', function($) {

  return {
    restrict: 'E',
    transclude: true,
    scope: {
   		weather: '=',
      	actions: '='
    },
    link: function(scope, element, attrs, ctrl, transclude, formCtrl) {
      scope.itemClick = function(a) {
        a.action();
      }

      scope.visible = false;
      scope.$root.$on("shop-show", function(data) {
        if (scope.visible) {
          hide();
        }
      });

      function show() {
        $(element).find("#div-side-screen").transition({
          right: '-10px'
        }, 700, 'ease');
        $(element).find("#home_arrow_main_menu").transition({
          rotate: '0deg'
        });
        scope.$root.$emit("side-show");
        scope.visible = true;
      }
      function hide() {
        $(element).find("#div-side-screen").transition({
          right: '-570px'
        }, 700, 'ease');
        $(element).find("#home_arrow_main_menu").transition({
          rotate: '180deg'
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
    templateUrl: '/public/templates/side-menu.html'
  };

}]);