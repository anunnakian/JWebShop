(function() {
    'use strict';

    angular
        .module('jWebShopApp')
        .controller('HomeController', HomeController)
        .controller('CategoryPageController', CategoryPageController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'Product'];
    CategoryPageController.$inject = ['$scope', '$rootScope', '$stateParams', 'entities'];

    function HomeController ($scope, Principal, LoginService, $state, Product) {
        var vm = this;

        vm.products = [];
        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        loadAll();
        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function register () {
            $state.go('register');
        }
        function loadAll() {
            Product.query(function(result) {
                vm.products = result;
            });
        }
    }

    function CategoryPageController($scope, $rootScope, $stateParams, entities) {
        var vm = this;
        vm.products = entities;
    }
})();
