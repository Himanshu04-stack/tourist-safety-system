#!/bin/bash

git pull origin main --rebase

if [[ -n $(git status -s) ]]; then
  git add .
  git commit -m "Auto sync: $(date)"
  git push
else
  echo "No changes to sync"
fi
