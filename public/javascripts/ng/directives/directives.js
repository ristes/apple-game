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
      scope.showNext = true;
      scope.visible = false;
      scope.buying = false;

      scope.itemClick = function(item) {
        if (!scope.buying) {
          scope.buying = true;
          scope.$root.$emit('buy-item', item);
          if (scope.onItemClick) {
            scope.onItemClick(item);
          }
        }
      };

      var unregBought = scope.$root.$on("item-bought", function($scope) {
        scope.buying = false;
      });

      var unregShow = scope.$root.$on("shop-show", function($scope, cfg) {
        scope.$root.$emit('side-hide');
        scope.items = cfg.items;
        scope.shop = cfg.shop;
        scope.storeUrl = cfg.storeUrl;
        if (cfg.showNext !== null) {
          scope.showNext = cfg.showNext;
          if (cfg.onHide) {
            scope.hideFn = cfg.onHide;
          }
        }
        if (cfg.onItemClick) {
          scope.onItemClick = cfg.onItemClick;
        }
        scope.visible = true;
      });
      
      scope.price = function(item) {
        if(item.perHa && item.price && scope.$root.plantation) {
          return item.price * scope.$root.plantation.area;
        } else {
          return item.price;
        }
      }

      var unregHide = scope.$root.$on("shop-hide", function() {
        scope.hide();
      });

      scope.hide = function() {
        scope.visible = false;
        scope.buying = false;
        if (scope.hideFn) {
          scope.hideFn();
        }
      }

      scope.$on("$destroy", function() {
        unregBought();
        unregHide();
        unregShow();
      });

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

      scope.show = function() {
        scope.$root.$emit("shop-hide");
        scope.bgw = 1366;
        scope.bgh = 768;
        scope.visible = true;
      }
      scope.hide = function() {
        scope.bgw = 0;
        scope.bgh = 0;
        scope.visible = false;
      }

      var un = scope.$root.$on("side-hide", function() {
        scope.hide();
      });

      scope.$on('$destroy', function() {
        if (un) {
          un();
        }
      })

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

}]).directive('weatherInfoDetails', ['jQuery', function($) {
  return {
    restrict: 'E',
    transclude: true,
    scope: {
      w: '=',
      visible: '='
    },
    link: function(scope, element, attrs, ctrl, transclude, formCtrl) {
      scope.visible = false;
      function hide() {
        // $(element).find("#weather-info-details").transition({
        // right : '-750px'
        // }, 700, 'ease');
        scope.visible = false;
        $(element).find("#weather-info-details").animate({
          opacity: 0
        });
      }

      function show(position) {
        // $(element).find("#weather-info-details").transition({
        // right : '350px'
        // }, 700, 'ease');
        scope.visible = true;
        $(element).find("#weather-info-details").css("top", position.top);
        $(element).find("#weather-info-details").css("left", position.left);
        $(element).find("#weather-info-details").animate({
          opacity: 1
        });
      }

      scope.$root.$on("weather-hide", function($scope) {
        hide();
      });
      scope.$root.$on("weather-show", function($scope, w, position) {
        scope.w = w;
        show(position);
      });

    },
    templateUrl: '/public/templates/weather-info-details.html'
  };
}]).directive(
        'weatherInfo',
        [
            'jQuery',
            function($) {
              return {
                restrict: 'E',
                transclude: true,
                scope: {
                  weather: '=',
                  detailWeather: '=',
                },

                link: function(scope, element, attrs, ctrl, transclude,
                        formCtrl) {

                  scope.$root.$emit("weather-hide");
                  scope.visible = false;
                  scope.detailsWeather = function(w) {
                    scope.toggle(w);
                    // scope.$root.$emit("weather-show", w);
                  };
                  scope.toggle = function(w) {
                    if (scope.visible == false) {
                      $(element).find(".weather-details-info-more-less").css(
                              "visibility", "hidden");
                      $(element)
                              .find("#weather-details-info-more-less-" + w.id)
                              .css("visibility", "visible");
                      $(element).find(
                              "#weather-details-info-more-less-span-" + w.id)
                              .text("-");
                      $(element)
                              .find("#weather-details-info-more-less-" + w.id)
                              .css("background-color", "cyan");
                      var position = $(element).find(
                              "#weather-details-info-more-less-" + w.id)
                              .position();
                      position.top = position.top
                              + $('.weather_wrapper').position().top
                              + $(element)
                                      .find(
                                              "#weather-details-info-more-less-"
                                                      + w.id).height() + 5;
                      position.left = $('.weather_wrapper').position().left
                              + position.left;
                      scope.$root.$emit("weather-show", w, position);
                      scope.visible = true;
                    } else {
                      $(element).find(".weather-details-info-more-less").css(
                              "visibility", "visible");
                      $(element).find(".weather-details-info-more-less-span")
                              .text("+");
                      $(element)
                              .find("#weather-details-info-more-less-" + w.id)
                              .css("background-color", "white");
                      scope.$root.$emit("weather-hide");
                      scope.visible = false;
                    }
                  };
                },
                templateUrl: '/public/templates/weather-info.html'
              };
            }]);