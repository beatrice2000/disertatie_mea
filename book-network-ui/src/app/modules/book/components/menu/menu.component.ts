import { Component } from '@angular/core';
import {KeycloakService} from "../../../../services/keycloak/keycloak.service";
import { Router } from '@angular/router';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent {


  constructor(
    private keycloakService: KeycloakService,
    private router: Router
  ){
  }

  searchQuery: string = '';
  advancedSearchQuery: string = '';
  userName: string = '';

  ngOnInit(): void {
    const linkColor = document.querySelectorAll('.nav-link');
    linkColor.forEach(link => {
      if (window.location.href.endsWith(link.getAttribute('href') || '')) {
        link.classList.add('active');
      }
      link.addEventListener('click', () => {
        linkColor.forEach(l => l.classList.remove('active'));
        link.classList.add('active');
      });
    });

    const tokenParsed = this.keycloakService.keycloak.tokenParsed;
    this.userName = tokenParsed?.['given_name'];
  }

  onSearch(): void {
    if (this.searchQuery.trim()) {
      this.router.navigate(['/books'], { queryParams: { query: this.searchQuery } });
    }
  }

  async logout() {
    this.keycloakService.logout();
  }

  onAdvancedSearch() {
    if (this.advancedSearchQuery.trim()) {
      this.router.navigate(['/books'], {
        queryParams: { advancequery: this.advancedSearchQuery }
      });
    }
  }
}
