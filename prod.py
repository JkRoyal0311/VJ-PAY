import os
import zipfile

def create_production_zip(output_filename="vjpay.zip"):
    # Folders and files to completely exclude from the production zip
    exclusions = {
        ".git",
        "node_modules",
        "target",          # Spring Boot target folder
        ".angular",        # Angular cache
        ".vscode",
        ".idea",
        "__pycache__",
        output_filename
    }

    current_dir = os.path.dirname(os.path.abspath(__file__))
    zip_path = os.path.join(current_dir, output_filename)

    print(f"Packaging project for production upload...")
    
    # Create the zip file
    with zipfile.ZipFile(zip_path, 'w', zipfile.ZIP_DEFLATED) as zipf:
        for root, dirs, files in os.walk(current_dir):
            # Modify dirs in-place to skip excluded directories entirely
            dirs[:] = [d for d in dirs if d not in exclusions]
            
            for file in files:
                if file in exclusions or file.endswith('.zip') or file.endswith('.pyc'):
                    continue
                
                file_path = os.path.join(root, file)
                # Calculate relative path to keep the folder structure clean
                arcname = os.path.relpath(file_path, current_dir)
                zipf.write(file_path, arcname)
                print(f"Added: {arcname}")

    print("-" * 40)
    print(f"Success! Your production code is safely zipped at:")
    print(f" -> {zip_path}")
    print(f"You can now upload '{output_filename}' to your GCP server (as mentioned in Step 4 of deploy.md).")

if __name__ == "__main__":
    create_production_zip()