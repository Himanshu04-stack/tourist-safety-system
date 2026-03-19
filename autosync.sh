#!/bin/bash

# Step 1: Add & commit changes first
if [[ -n $(git status -s) ]]; then
  git add .
  git commit -m "Auto sync: $(date)"
else
  echo "No changes to commit"
fi

# Step 2: Pull latest changes safely
git pull origin main --rebase

# Step 3: Push to GitHub
git push
