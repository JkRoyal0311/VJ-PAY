// You only need this ONE environment file!
//
// Change `customApiUrl` below to your backend IP if you need to hardcode it (e.g., 'http://34.123.45.67:8080').
// If you leave it blank, the app will automatically use 'http://localhost:8080' for local testing
// and use dynamic proxy paths when deployed via Docker/Nginx!

const customApiUrl: string = ''; // <-- Leave this blank! Nginx handles the routing for you.

export const environment = {
  production: customApiUrl.length > 0,
  get apiUrl() {
    // 1. If user provided a custom URL, use it
    if (customApiUrl) {
      return customApiUrl;
    }

    // 2. Otherwise dynamically check: if running locally, use localhost:8080.
    // If not local, return empty string so Nginx proxies the requests seamlessly.
    return (window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1')
      ? 'http://localhost:8080'
      : '';
  }
};
