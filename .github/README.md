# YouTube Playlists Management API

## Description

The YouTube Playlists Management API is a tool designed to help us manage our YouTube
playlists more efficiently. It allows to perform bulk edits on playlists via rest
calls.

## Main Feature

**Bulk Edits**: Easily perform bulk edits on your playlists.

## Getting Started Locally

### Prerequisites

- Java 17
- Docker Daemon

### Starting up

1. Clone the repository

```bash
git clone https://github.com/leingenm/ypm.git
```

2. Build the project

```bash
./gradlew build
```

3. Set up env variables

`DB_NAME` - DB name, can be started from compose.yaml

`POSTGRES_PASSWORD` - DB password

`POSTGRES_USERNAME` - DB username

4. Run the project

```bash
./gradlew bootRun
```

## Usage

Set up ur own auth 2.0 auth app
in [Google Console](https://support.google.com/googleapi/answer/6158849?hl=en#zippy=), don't
forget ot add `localhost:8080` or `ypmngr.xyz` to redirect uris and ur email as a test user.

Obtain ur token via our swagger specifying ur `client_id` and `client_secret` or via postman and use
bearer token auth.

`Auth URL` - `https://accounts.google.com/o/oauth2/v2/auth`

`Access Token URL` - `https://www.googleapis.com/oauth2/v4/token`

`Scope` must include `https://www.googleapis.com/auth/youtube`, double check if the scope is present
in ur google console app Data access tab

## Contributing

We welcome contributions from the community. Please read
our [Contributing Guide](https://github.com/leingenm/ypm/blob/main/.github/CONTRIBUTING.md) for
more information on how to contribute to our project.

## License

This project is licensed under the MIT License. See
the [LICENSE](https://github.com/leingenm/ypm/blob/main/LICENSE) file for details.

## Contact

If you have any questions or feedback, please feel free to contact us or create an issue or bug
request in [Issues](https://github.com/leingenm/ypm/issues/new/choose).
