(function() {
    'use strict';

    angular
        .module('jWebShopApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'Product'];

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
})();
