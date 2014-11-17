GameDirectives.directive('gShopMenu', ['jQuery', 'BoughtItems', function($, BoughtItems) {

  return {
    restrict: 'E',
    transclude: true,
    scope: {
      exposeName: '@',
      onItemClick: '='
    },
    link: function(scope, element, attrs, ctrl, transclude, formCtrl) {
      scope.showNext = true;
      scope.visible = false;
      scope.buying = false;

      scope.itemClick = function(item, callback) {
        if (!scope.buying) {
          scope.buying = true;

          if (typeof scope.onItemClick === 'function') {
            var hide = scope.onItemClick(item);
          }
        }
      };


      var unregBought = scope.$root.$on("item-bought", function($scope) {
        if (scope.$root.day.field) {
          BoughtItems.load();
        }
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
        if (item.perHa && item.price && scope.$root.plantation) {
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

      scope.$parent[scope.exposeName] = scope;

    },
    templateUrl: '/public/templates/shop-menu.html'
  };

}])
