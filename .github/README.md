# YouTube Playlists Management API

## Description

The YouTube Playlists Management API is a powerful tool designed to help you manage your YouTube
playlists more efficiently. With our API, you can perform bulk edits on your playlists via HTTP calls.

## Main Feature

**Bulk Edits**: Easily perform bulk edits on your playlists. Add, remove, or reorder videos in
  your playlists in a single HTTP call.

## Getting Started Locally

### Prerequisites

- Java 17 or higher

### Installation

1. Clone the repository
```bash
git clone https://github.com/RomanMager/ypm.git
```

2. Build the project
```bash
./gradlew build
```

3. Run the project
```bash
./gradlew bootRun
```

## Usage

1. Open the browser and navigate to `https://ypmngr.xyz/`
2. You will be redirected to the Google login page
3. After successful login, you will be redirected back to the `ypmngr.xyz` with a jsession id in your
   browsers cookies (we are not ready to deploy the app to the Google
   cloud yet, so we use the specified Google accounts for testing, if you want to test the app,
   please contact the maintainers)
4. Extract it and use it in the following requests as a `JSESSIONID` header

The login procedure is the most unfriendly part of the app, if you have any ideas, please share them
with us.

### For local development

Check out [How to use HTTP files in IntelliJ IDEA](https://github.com/RomanMager/ypm/blob/main/docs/how_to_use_http_files.md).

### API Endpoints

<a href="https://www.postman.com/ypmanager/workspace/ypm-api"><img alt="Run in Postman" src="https://run.pstmn.io/button.svg"/></a>

### How To Run Tests

```bash
./gradlew test
```

## Contributing

We welcome contributions from the community. Please read
our [Contributing Guide](https://github.com/RomanMager/ypm/blob/main/.github/CONTRIBUTING.md) for
more information on how to contribute to our project.

## License

This project is licensed under the MIT License. See
the [LICENSE](https://github.com/RomanMager/ypm/blob/main/LICENSE) file for details.


## Contact

If you have any questions or feedback, please feel free to contact us or create an issue or bug
request in [Issues](https://github.com/RomanMager/ypm/issues/new/choose).
