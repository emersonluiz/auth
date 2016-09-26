export class ConfigService {

    private server_domain: string = 'http://localhost:';
    private auth_url: string = '8080/auth-rest-api';
    private client_url: string = '8081/auth-client-example';

    public authUrl(): string {
        return this.server_domain + this.auth_url;
    }

    public clientUrl(): string {
        return this.server_domain + this.client_url;
    }
}