'use strict';

/* Directives */

var GameDirectives = angular
        .module(
                'Game.directives',
                ['ngResource', 'ngRoute', 'ngAnimate', 'ngCookies',
                    'ui.bootstrap', 'toaster',
                    'pascalprecht.translate'])
        .directive('appVersion', ['version', function(version) {
          return function(scope, elm, attrs) {
            elm.text(version);
          };
        }])
        .directive(
                'progressDialog',
                [
                    'jQuery',
                    '$modal',
                    function($, $modal) {
                      return {
                        transclude: true,
                        restrict: 'E',
                        scope: {
                          prefix: '@'
                        },
                        link: function(scope, element, attrs, ctrl, transclude,
                                formCtrl) {
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
                              var prog = scope.$root.$on('progress-'
                                      + scope.prefix, onProgress);
                              listeners.push(prog);
                            } else {
                              var step = 100 / cfg.duration;
                              scope.timer = setInterval(function() {
                                onProgress(scope, step);
                              }, 1000);

                            }
                            dialog.show();
                          }

                          var show = scope.$root.$on('show-progress-'
                                  + scope.prefix, onShow);
                          listeners.push(show);

                          scope.$on("$destroy", function() {
                            for (var i = 0; i < listeners.length; i++) {
                              listeners[i]();
                            }
                          });
                        },
                        template: '<div></div>'
                      };
                    }])
        .directive('userInfo', ['jQuery', function($) {

          return {
            restrict: 'E',
            transclude: true,
            scope: {},
            link: function(scope, element, attrs, ctrl, transclude, formCtrl) {
            	
              $.cssEase['bounce'] = 'cubic-bezier(0,1,0.5,1.3)';
              $(element).find("#div_user_icon").transition({
                left: '15px'
              }, 1000, 'ease');
              $(element).find("#div_apple_in_stock_info").transition({
                top: '-10px'
              }, 1000, 'bounce');
              $(element).find("#div_money_in_stock_info").transition({
                top: '-10px'
              }, 1000, 'bounce');
              $(element).find("#div_eco_points_info").transition({
                top: '-10px'
              }, 1000, 'bounce');
              
             
            },
            templateUrl: '/public/templates/user-info.html'
          };

        }])
        .directive(
                'weatherInfoDetails',
                [
                    'jQuery',
                    function($) {
                      return {
                        restrict: 'E',
                        transclude: true,
                        scope: {
                          w: '='
                        },
                        link: function(scope, element, attrs, ctrl, transclude,
                                formCtrl) {
                          scope.weather_info_visible = false;
                          function weather_detail_info_hide() {
                            // $(element).find("#weather-info-details").transition({
                            // right : '-750px'
                            // }, 700, 'ease');
                            scope.weather_info_visible = false;
                            $(element).find("#weather-info-details").animate({
                              opacity: 0
                            });
                          }

                          function weather_detail_info_show(position) {
                            // $(element).find("#weather-info-details").transition({
                            // right : '350px'
                            // }, 700, 'ease');
                        	 scope.weather_info_visible = true;
                            $(element).find("#weather-info-details").css("top",
                                    position.top);
                            $(element).find("#weather-info-details").css(
                                    "left", position.left);
                            $(element).find("#weather-info-details").animate({
                              opacity: 1
                            });
                          }

                          scope.$root.$on("weather-hide", function($scope) {
                        	  weather_detail_info_hide()
                          });
                          scope.$root.$on("weather-show", function($scope, w,
                                  position) {
                            scope.w = w;
                            weather_detail_info_show(position)
                          });

                        },
                        templateUrl: '/public/templates/weather-info-details.html'
                      };
                    }])
        .directive(
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
                          };
                          scope.toggle = function(w) {
                            if (scope.visible == false) {
                              $(element)
                                      .find(".weather-details-info-more-less")
                                      .css("visibility", "hidden");
                              $(element)
                                      .find(
                                              "#weather-details-info-more-less-"
                                                      + w.id).css("visibility",
                                              "visible");
                              $(element).find(
                                      "#weather-details-info-more-less-span-"
                                              + w.id).text("-");
                              $(element)
                                      .find(
                                              "#weather-details-info-more-less-"
                                                      + w.id).css(
                                              "background-color", "cyan");
                              var position = $(element)
                                      .find(
                                              "#weather-details-info-more-less-"
                                                      + w.id).position();
                              position.top = position.top
                                      + $('.weather_wrapper').position().top
                                      + $(element).find(
                                              "#weather-details-info-more-less-"
                                                      + w.id).height() + 5;
                              position.left = $('.weather_wrapper').position().left
                                      + position.left;
                              scope.$root.$emit("weather-show", w, position);
                              scope.visible = true;
                            } else {
                              $(element)
                                      .find(".weather-details-info-more-less")
                                      .css("visibility", "visible");
                              $(element).find(
                                      ".weather-details-info-more-less-span")
                                      .text("+");
                              $(element)
                                      .find(
                                              "#weather-details-info-more-less-"
                                                      + w.id).css(
                                              "background-color", "white");
                              scope.$root.$emit("weather-hide");
                              scope.visible = false;
                            }
                          };
                        },
                        templateUrl: '/public/templates/weather-info.html'
                      };
                    }]);
