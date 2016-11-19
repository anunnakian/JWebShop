(function() {
    'use strict';

    angular
        .module('jWebShopApp')
        .controller('TopMenuController', TopMenuController);

    TopMenuController.$inject = ['$state', 'Principal', 'ProfileService', 'Category'];

    function TopMenuController ($state, Principal, ProfileService, Category) {
        var vm = this;

        vm.categories = [];
        vm.isNavbarCollapsed = true;
        vm.isAuthenticated = Principal.isAuthenticated;

        ProfileService.getProfileInfo().then(function(response) {
            vm.inProduction = response.inProduction;
            vm.swaggerEnabled = response.swaggerEnabled;
        });

        vm.toggleNavbar = toggleNavbar;
        vm.collapseNavbar = collapseNavbar;
        vm.$state = $state;

        loadAll();

        function toggleNavbar() {
            vm.isNavbarCollapsed = !vm.isNavbarCollapsed;
        }

        function collapseNavbar() {
            vm.isNavbarCollapsed = true;
        }

        function loadAll() {
            Category.query(function(result) {
                vm.categories = result;
            });
        }
    }
})();
