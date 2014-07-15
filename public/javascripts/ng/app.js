'use strict';

// Declare app level module which depends on filters, and services
var Game = angular.module('Game', ['Game.filters', 'Game.services',
    'Game.directives']);

Game.config([
    '$routeProvider',
    '$translateProvider',
    '$httpProvider',
    '$locationProvider',
    function($routeProvider, $translateProvider, $httpProvider,
            $locationProvider) {

      // Initialize angular-translate
      $translateProvider.useStaticFilesLoader({
        prefix: '/public/i18n/',
        suffix: '.json'
      });

      $translateProvider.preferredLanguage('en');

      $translateProvider.useCookieStorage();

    }]);

Game.run(['$rootScope', '$location', '$farmer', '$items','$day','$plantation',
    function($rootScope, $location, $farmer, $items, $day, $plantation) {
      $farmer.load();
      $day.load($farmer);
      $plantation.load();
      $rootScope.next = function() {
    	  $day.next();
    	  $rootScope.$emit("weather-hide");
      };
      $rootScope.nextWeek = function() {
    	  $day.nextWeek();
    	  $rootScope.$emit("weather-hide");
      };
      $rootScope.nextMonth = function() {
    	  $day.nextMonth();
    	  $rootScope.$emit("weather-hide");
      };
      $location.path("/");
    }]);
