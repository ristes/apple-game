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
});