#!/usr/bin/env node

const fs = require('fs');

const migrationsPath = './src/main/resources/db/migration';

let migrationName = process.argv[2];

if (!migrationName) {
  throw new Error('please specify the name of the migration file! Eg: create-users-schema');
}

migrationName = migrationName.replace(/-/g, '_');

const repeatableMigrationArg = process.argv[3];

let repeatableMigration = false;
if (repeatableMigrationArg === '--repeatable') {
    repeatableMigration = true;
}

fs.readdir(migrationsPath, (err, files) => {
  if (err) {
    throw err;
  }

  if (repeatableMigration) {
    fs.closeSync(fs.openSync(`${migrationsPath}/R__${migrationName}.sql`, 'wx'));
  } else {
    let nextVersion = 1;
      if (files.length !== 0) {
        const filesDescSorted = files.filter(f => !f.startsWith('R')).sort((a, b) => {
          const aV = a.split('__')[0];
          const bV = b.split('__')[0];
          return parseInt(bV.substring(1)) - parseInt(aV.substring(1));
        });
        nextVersion = parseInt(filesDescSorted[0].split("__")[0].substring(1)) + 1;
      }
      fs.closeSync(fs.openSync(`${migrationsPath}/V${nextVersion}__${migrationName}.sql`, 'wx'));
  }
});
