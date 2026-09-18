Viewed styles.css:26-50

Here are the exact step-by-step instructions to deploy your application to Google Cloud Platform (GCP). Because we have already set up the `docker-compose.yml` and `Dockerfiles`, and automated the environment variables, this process is incredibly streamlined!

### Step 1: Create a GCP Compute Engine VM
1. Go to the [Google Cloud Console](https://console.cloud.google.com/).
2. Navigate to **Compute Engine** > **VM instances**.
3. Click **Create Instance**.
4. **Name**: `vjpay-server`
5. **Machine type**: Select `e2-medium` (this is recommended since you are running Spring Boot, Angular, and MySQL concurrently).
6. **Boot disk**: Change the OS to **Ubuntu 22.04 LTS** (Size: ~20GB).
7. **Firewall**: Check both **Allow HTTP traffic** and **Allow HTTPS traffic**.
8. Click **Create**.

### Step 2: Open Firewall for the Backend API (Port 8080)
Because your backend runs on port 8080, we need to allow traffic to it.
1. In the GCP sidebar, go to **VPC network** > **Firewall**.
2. Click **Create Firewall Rule**.
3. **Name**: `allow-8080`
4. **Targets**: Select `All instances in the network`.
5. **Source IPv4 ranges**: Type `0.0.0.0/0` (this allows anyone on the internet to reach your API).
6. **Protocols and ports**: Check `tcp` and type `8080`.
7. Click **Create**.

### Step 3: Install Docker on your new Server
1. Go back to your **VM instances** page.
2. Click the **SSH** button next to your `vjpay-server`. Note the **External IP** address of your server.
3. In the black SSH terminal that opens, copy and paste these commands one by one to install Docker:
   ```bash
   sudo apt update -y
   sudo apt upgrade -y
   sudo apt install unzip -y
   sudo apt install docker.io docker-compose -y
   sudo systemctl enable docker
   sudo systemctl start docker
   sudo usermod -aG docker $USER
   newgrp docker
   ```

### Step 4: Transfer Your Code to the Server
You need to copy your `Payment-WalletApplication-main` folder from your local computer to the GCP server. You can do this securely via Git:
```bash
# In the GCP SSH Terminal:
git clone <URL_TO_YOUR_GITHUB_REPO> vjpay
cd vjpay
```
*(Alternatively, you can zip your folder locally, click the "Upload file" button in the top right of the GCP SSH window to upload it, and then run `unzip` on the server).*

### Step 5: Start the "All-in-One" Deployment
Because I already configured everything perfectly for you, you just need to run one single command to launch the entire stack:
```bash
docker-compose up -d --build
```

**What happens now?**
Docker will read the `docker-compose.yml` file I made. It will automatically:
- Download and boot up a secure MySQL 8.0 database.
- Compile your Spring Boot application using Maven and connect it to the database.
- Compile your Angular application for production and launch it inside an ultra-fast Nginx web server.

### Step 6: Access Your Live Application!
Once the command finishes building:
- Open your browser and go to `http://<YOUR_GCP_VM_EXTERNAL_IP>` to see the VJ Pay Angular frontend.
- (You do NOT need to update any hardcoded IP addresses or environments! The dynamic environment file and Nginx proxy I built will automatically handle routing your frontend's API calls internally!)