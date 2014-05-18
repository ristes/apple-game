'use strict';

/* Directives */

angular.module(
        'Game.directives',
        ['ngResource', 'ngRoute', 'ngAnimate', 'ngCookies', 'ui.bootstrap',
            'toaster', 'mgcrea.ngStrap', 'pascalprecht.translate']).directive(
        'appVersion', ['version', function(version) {
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
}).directive('progressDialog', ['jQuery', '$modal', function($, $modal) {
  return {
    transclude: true,
    restrict: 'E',
    scope: {
      prefix: '@'
    },
    link: function(scope, element, attrs, ctrl, transclude, formCtrl) {
      var listeners = [];

      var dialog = $modal({
        scope: scope,
        backdrop: 'static',
        template: '/public/templates/progress-dialog.html',
        show: false
      });

      function onProgress(_scope, v) {
        scope.$apply(function() {
          scope.status += v;
        });
        if (scope.status > 100) {
          clearInterval(scope.timer);
          onHide();
        }
      }

      function onHide() {
        dialog.hide();
        scope.$root.$emit('hide-progress-' + scope.prefix);
      }

      function onShow(_scope, cfg) {
        scope.status = 0;
        scope.title = cfg.title;

        if (cfg.waitProgress) {
          var prog = scope.$root.$on('progress-' + scope.prefix, onProgress);
          listeners.push(prog);
        } else {
          var step = 100 / cfg.duration;
          scope.timer = setInterval(function() {
            onProgress(scope, step);
          }, 1000);

        }
        dialog.show();
      }

      var show = scope.$root.$on('show-progress-' + scope.prefix, onShow);
      listeners.push(show);

      scope.$on("$destroy", function() {
        for (var i = 0; i < listeners.length; i++) {
          listeners[i]();
        }
      });
    },
    template: '<div></div>'
  };
}]).directive('userInfo', ['jQuery', function($) {

  return {
    restrict: 'E',
    transclude: true,
    scope: {},
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
    scope: {},
    link: function(scope, element, attrs, ctrl, transclude, formCtrl) {
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
        scope.$root.$emit('operation-' + a.name, a);
      }
      scope.bgw = 0;
      scope.bgh = 0;

      scope.visible = false;
      scope.$root.$on("shop-show", function(data) {
        if (scope.visible) {
          scope.hide();
        }
      });

      scope.mainDiv = $(element).find("#div-side-screen");
      scope.btn = $(element).find("#home_arrow_main_menu");
      scope.show = function() {
        scope.bgw = 1366;
        scope.bgh = 768;
        scope.mainDiv.transition({
          right: '-10px'
        }, 700, 'ease');

        scope.btn.transition({
          rotate: '0deg'
        });
        scope.$root.$emit("side-show");
        scope.visible = true;
      }
      scope.hide = function() {
        scope.bgw = 0;
        scope.bgh = 0;
        scope.mainDiv.transition({
          right: '-570px'
        }, 700, 'ease');
        scope.btn.transition({
          rotate: '180deg'
        });
        scope.$root.$emit("side-hide");
        scope.visible = false;
      }

      scope.onClick = function() {
        if (scope.visible) {
          scope.hide();
        } else {
          scope.show();
        }
      }
    },
    templateUrl: '/public/templates/side-menu.html'
  };

}]);