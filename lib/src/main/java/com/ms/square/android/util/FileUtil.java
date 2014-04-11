package com.ms.square.android.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class FileUtil {

    private static final String TAG = FileUtil.class.getSimpleName();

    /**
     * The minimum allowed disk space in megabytes. File creation methods will throw
     * {@link LowDiskSpaceException} if the usable disk space in desired partition is less than
     * this amount.
     */
    private static final long MIN_DISK_SPACE_MB = 100;
    /** The min disk space in bytes */
    private static final long MIN_DISK_SPACE = MIN_DISK_SPACE_MB * 1024 * 1024;

    /**
     * Thrown if usable disk space is below minimum threshold.
     */
    public static class LowDiskSpaceException extends RuntimeException {
        LowDiskSpaceException(String msg, Throwable cause) {
            super(msg, cause);
        }
        LowDiskSpaceException(String msg) {
            super(msg);
        }
    }

    /**
     * Helper function to create a temp directory in the system default temporary file directory.
     *
     * @param prefix The prefix string to be used in generating the file's name; must be at least
     *            three characters long
     * @return the created directory
     * @throws IOException if file could not be created
     */
    public static File createTempDir(String prefix) throws IOException {
        return createTempDir(prefix, null);
    }

    /**
     * Helper function to create a temp directory.
     *
     * @param prefix The prefix string to be used in generating the file's name; must be at least
     *            three characters long
     * @param parentDir The parent directory in which the directory is to be created. If
     *            <code>null</code> the system default temp directory will be used.
     * @return the created directory
     * @throws IOException if file could not be created
     */
    public static File createTempDir(String prefix, File parentDir) throws IOException {
        // create a temp file with unique name, then make it a directory
        File tmpDir = File.createTempFile(prefix, "", parentDir);
        tmpDir.delete();
        if (!tmpDir.mkdirs()) {
            throw new IOException("unable to create directory");
        }
        return tmpDir;
    }

    /**
     * Helper wrapper function around {@link File#createTempFile(String, String)} that audits for
     * potential out of disk space scenario.
     *
     * @see {@link File#createTempFile(String, String)}
     * @throws LowDiskSpaceException if disk space on temporary partition is lower than minimum
     *             allowed
     */
    public static File createTempFile(String prefix, String suffix) throws IOException {
        File returnFile = File.createTempFile(prefix, suffix);
        verifyDiskSpace(returnFile);
        return returnFile;
    }

    /**
     * Helper wrapper function around {@link File#createTempFile(String, String, File parentDir)}
     * that audits for potential out of disk space scenario.
     *
     * @see {@link File#createTempFile(String, String, File)}
     * @throws LowDiskSpaceException if disk space on partition is lower than minimum allowed
     */
    public static File createTempFile(String prefix, String suffix, File parentDir)
            throws IOException {
        File returnFile = File.createTempFile(prefix, suffix, parentDir);
        verifyDiskSpace(returnFile);
        return returnFile;
    }

    /**
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public static String readTextFile(String filePath) throws IOException {
        File file = new File(filePath);
        StringBuilder result = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;

        while ((line = br.readLine()) != null) {
            result.append(line).append('\n');
        }
        return result.toString();
    }

    /**
     *
     * @param context
     * @param fileName
     * @return
     * @throws IOException
     */
    public static String readTextFileFromAsset(Context context, String fileName) throws IOException {
        InputStream inputStream = context.getAssets().open(fileName);
        InputStreamReader inputReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputReader);
        String line;
        StringBuilder text = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            text.append(line).append('\n');
        }
        return text.toString();
    }

    /**
     *
     * @param context
     * @param resourceId
     * @return
     * @throws IOException
     */
    public static String readTextFileFromResource(Context context, int resourceId) throws IOException {
        StringBuilder body = new StringBuilder();

        try {
            InputStream inputStream =
                    context.getResources().openRawResource(resourceId);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String nextLine;
            while ((nextLine = bufferedReader.readLine()) != null) {
                body.append(nextLine).append('\n');
            }
        } catch (Resources.NotFoundException nfe) {
            Log.e(TAG, "Resource not found: " + resourceId, nfe);
        }

        return body.toString();
    }

    /**
     * A helper method that copies a file's contents to a local file
     *
     * @param origFile the original file to be copied
     * @param destFile the destination file
     * @throws IOException if failed to copy file
     */
    public static void copyFile(File origFile, File destFile) throws IOException {
        FileChannel srcChannel = new FileInputStream(origFile).getChannel();
        FileChannel destChannel = new FileOutputStream(destFile).getChannel();

        srcChannel.transferTo(0, srcChannel.size(), destChannel);
    }

    /**
     * Recursively copy folder contents.
     * <p/>
     * Only supports copying of files and directories - symlinks are not copied.
     *
     * @param sourceDir the folder that contains the files to copy
     * @param destDir the destination folder
     * @throws IOException
     */
    public static void recursiveCopy(File sourceDir, File destDir) throws IOException {
        for (File childFile : sourceDir.listFiles()) {
            File destChild = new File(destDir, childFile.getName());
            if (childFile.isDirectory()) {
                if (!destChild.mkdir()) {
                    throw new IOException(String.format("Could not create directory %s",
                            destChild.getAbsolutePath()));
                }
                recursiveCopy(childFile, destChild);
            } else if (childFile.isFile()) {
                copyFile(childFile, destChild);
            }
        }
    }

    /**
     * A helper method for writing string data to file
     *
     * @param inputString the input {@link String}
     * @param destFile the dest file to write to
     */
    public static void writeToFile(String inputString, File destFile) throws IOException {
        writeToFile(new ByteArrayInputStream(inputString.getBytes()), destFile);
    }

    /**
     * A helper method for writing string data to file at the specified path
     *
     * @param inputString the input {@link String}
     * @param filePath the absolute file path of the destination file to write to
     */
    public static void writeToFile(String inputString, String filePath) throws IOException {
        writeToFile(new ByteArrayInputStream(inputString.getBytes()), new File(filePath));
    }

    /**
     * A helper method for writing stream data to file
     *
     * @param input the unbuffered input stream
     * @param destFile the dest file to write to
     */
    public static void writeToFile(InputStream input, File destFile) throws IOException {
        InputStream origStream = null;
        OutputStream destStream = null;
        try {
            origStream = new BufferedInputStream(input);
            destStream = new BufferedOutputStream(new FileOutputStream(destFile));
            StreamUtil.copyStreams(origStream, destStream);
        } finally {
            StreamUtil.closeStream(origStream);
            StreamUtil.closeStream(destStream);
        }
    }

    /**
     * Utility method to extract entire contents of zip file into given directory
     *
     * @param zipFile the {@link ZipFile} to extract
     * @param destDir the local dir to extract file to
     * @throws IOException if failed to extract file
     */
    public static void extractZip(ZipFile zipFile, File destDir) throws IOException {
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            File childFile = new File(destDir, entry.getName());
            if (entry.isDirectory()) {
                childFile.mkdirs();
            } else {
                FileUtil.writeToFile(zipFile.getInputStream(entry), childFile);
            }
        }
    }

    /**
     * Utility method to create a temporary zip file containing the given directory and
     * all its contents.
     *
     * @param dir the directory to zip
     * @return a temporary zip {@link File} containing directory contents
     * @throws IOException if failed to create zip file
     */
    public static File createZip(File dir) throws IOException {
        File zipFile = FileUtil.createTempFile("dir", ".zip");
        ZipOutputStream out = null;
        try {
            FileOutputStream fileStream = new FileOutputStream(zipFile);
            out = new ZipOutputStream(new BufferedOutputStream(fileStream));
            addToZip(out, dir, new LinkedList<String>());
        } catch (IOException e) {
            zipFile.delete();
            throw e;
        } catch (RuntimeException e) {
            zipFile.delete();
            throw e;
        } finally {
            StreamUtil.closeStream(out);
        }
        return zipFile;
    }

    /**
     * Gets the extension for given file name.
     *
     * @param fileName
     * @return the extension or empty String if file has no extension
     */
    public static String getExtension(String fileName) {
        int index = fileName.lastIndexOf('.');
        if (index == -1) {
            return "";
        } else {
            return fileName.substring(index);
        }
    }

    /**
     * Gets the base name, without extension, of given file name.
     * <p/>
     * e.g. getBaseName("file.txt") will return "file"
     *
     * @param fileName
     * @return the base name
     */
    public static String getBaseName(String fileName) {
        int index = fileName.lastIndexOf('.');
        if (index == -1) {
            return fileName;
        } else {
            return fileName.substring(0, index);
        }
    }

    /**
     * Utility method to do byte-wise content comparison of two files.
     *
     * @return <code>true</code> if file contents are identical
     */
    public static boolean compareFileContents(File file1, File file2) throws IOException {
        BufferedInputStream stream1 = null;
        BufferedInputStream stream2 = null;

        try {
            stream1 = new BufferedInputStream(new FileInputStream(file1));
            stream2 = new BufferedInputStream(new FileInputStream(file2));
            boolean eof = false;
            while (!eof) {
                int byte1 = stream1.read();
                int byte2 = stream2.read();
                if (byte1 != byte2) {
                    return false;
                }
                eof = byte1 == -1;
            }
            return true;
        } finally {
            StreamUtil.closeStream(stream1);
            StreamUtil.closeStream(stream2);
        }
    }

    /**
     * Recursively delete given directory and all its contents
     */
    public static void recursiveDelete(File rootDir) {
        if (rootDir.isDirectory()) {
            File[] childFiles = rootDir.listFiles();
            if (childFiles != null) {
                for (File child : childFiles) {
                    recursiveDelete(child);
                }
            }
        }
        rootDir.delete();
    }

    /**
     * Try to delete a file.
     * @param file - may be null.
     * @return <code>true</code> upon successful deletion of the file.
     */
    public static boolean deleteFile(File file) {
        if (file != null) {
            return file.delete();
        }
        return false;
    }

    /**
     * Try to delete a file at the specified absolute path.
     * @param filePath - may be null.
     * @return <code>true</code> upon successful deletion of the file.
     */
    public static boolean deleteFile(String filePath) {
        if (filePath == null) return false;
        File fileToDelete = new File(filePath);
        return fileToDelete.delete();
    }

    private static void verifyDiskSpace(File file) {
        // Based on empirical testing File.getUsableSpace is a low cost operation (~ 100 us for
        // local disk, ~ 100 ms for network disk). Therefore call it every time tmp file is
        // created
        if (file.getUsableSpace() < MIN_DISK_SPACE) {
            throw new LowDiskSpaceException(String.format(
                    "Available space on %s is less than %s MB", file.getAbsolutePath(),
                    MIN_DISK_SPACE_MB));
        }
    }

    /**
     * Recursively adds given file and its contents to ZipOutputStream
     *
     * @param out the {@link ZipOutputStream}
     * @param file the {@link File} to add to the stream
     * @param relativePathSegs the relative path of file, including separators
     * @throws IOException if failed to add file to zip
     */
    private static void addToZip(ZipOutputStream out, File file, List<String> relativePathSegs)
            throws IOException {
        relativePathSegs.add(file.getName());
        if (file.isDirectory()) {
            // note: it appears even on windows, ZipEntry expects '/' as a path separator
            relativePathSegs.add("/");
        }
        ZipEntry zipEntry = new ZipEntry(buildPath(relativePathSegs));
        out.putNextEntry(zipEntry);
        if (file.isFile()) {
            writeToStream(file, out);
        }
        out.closeEntry();
        if (file.isDirectory()) {
            // recursively add contents
            File[] subFiles = file.listFiles();
            if (subFiles == null) {
                throw new IOException(String.format("Could not read directory %s",
                        file.getAbsolutePath()));
            }
            for (File subFile : subFiles) {
                addToZip(out, subFile, relativePathSegs);
            }
            // remove the path separator
            relativePathSegs.remove(relativePathSegs.size()-1);
        }
        // remove the last segment, added at beginning of method
        relativePathSegs.remove(relativePathSegs.size()-1);
    }

    /**
     * Helper method to write input file contents to output stream.
     *
     * @param file the input {@link File}
     * @param out the {@link OutputStream}
     *
     * @throws IOException
     */
    private static void writeToStream(File file, OutputStream out) throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(file));
            StreamUtil.copyStreams(inputStream, out);
        } finally {
            StreamUtil.closeStream(inputStream);
        }
    }

    /**
     * Builds a file system path from a stack of relative path segments
     *
     * @param relativePathSegs the list of relative paths
     * @return a {@link String} containing all relativePathSegs
     */
    private static String buildPath(List<String> relativePathSegs) {
        StringBuilder pathBuilder = new StringBuilder();
        for (String segment : relativePathSegs) {
            pathBuilder.append(segment);
        }
        return pathBuilder.toString();
    }
}