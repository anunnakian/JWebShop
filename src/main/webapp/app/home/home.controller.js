(function() {
    'use strict';

    angular
        .module('jWebShopApp')
        .controller('HomeController', HomeController)
        .controller('CategoryPageController', CategoryPageController);

    HomeController.$inject = ['$scope', '$rootScope', '$state', 'Product'];
    CategoryPageController.$inject = ['$scope', '$rootScope', '$stateParams', 'entities'];

    function HomeController ($scope, $rootScope, $state, Product) {
        var vm = this;

        vm.products = [];

        loadAll();

        function loadAll() {
            Product.query(function(result) {
                vm.products = result;
            });
        }

        $rootScope.$on('ngCart:checkout_succeeded', function(data){
            
        });

        $rootScope.$on('ngCart:checkout_failed', function(statusCode, error){
            console.log(error);
        });
    }

    function CategoryPageController($scope, $rootScope, $stateParams, entities) {
        var vm = this;
        vm.products = entities;
    }
})();
