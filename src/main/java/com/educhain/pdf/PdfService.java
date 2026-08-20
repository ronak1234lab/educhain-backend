package com.educhain.pdf;

import com.educhain.entity.Certificate;
import com.educhain.qr.QrCodeService;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfService {

    private final QrCodeService qrCodeService;

    // ==========================================
    // Constructor
    // ==========================================

    public PdfService(QrCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }

    // ==========================================
    // Generate Certificate PDF
    // ==========================================

    public byte[] generateCertificatePdf(
            Certificate certificate) {

        try {

            Document document =
                    new Document();

            ByteArrayOutputStream out =
                    new ByteArrayOutputStream();

            PdfWriter.getInstance(
                    document,
                    out
            );

            document.open();

            // ==========================================
            // Fonts
            // ==========================================

            Font titleFont =
                    new Font(
                            Font.FontFamily.HELVETICA,
                            22,
                            Font.BOLD
                    );

            Font normalFont =
                    new Font(
                            Font.FontFamily.HELVETICA,
                            14
                    );

            Font transactionFont =
                    new Font(
                            Font.FontFamily.HELVETICA,
                            10
                    );

            // ==========================================
            // Certificate Title
            // ==========================================

            document.add(
                    new Paragraph(
                            "CERTIFICATE OF COMPLETION",
                            titleFont
                    )
            );

            document.add(
                    new Paragraph(" ")
            );

            // ==========================================
            // Student Information
            // ==========================================

            document.add(
                    new Paragraph(
                            "This is to certify that",
                            normalFont
                    )
            );

            document.add(
                    new Paragraph(
                            certificate
                                    .getStudent()
                                    .getStudentName(),
                            titleFont
                    )
            );

            document.add(
                    new Paragraph(
                            "has successfully completed"
                    )
            );

            // ==========================================
            // Course
            // ==========================================

            document.add(
                    new Paragraph(
                            certificate
                                    .getCourse()
                                    .getCourseName(),
                            titleFont
                    )
            );

            // ==========================================
            // University
            // ==========================================

            document.add(
                    new Paragraph(
                            "University"
                    )
            );

            document.add(
                    new Paragraph(
                            certificate
                                    .getCourse()
                                    .getUniversity()
                                    .getUniversityName()
                    )
            );

            document.add(
                    new Paragraph(" ")
            );

            // ==========================================
            // Certificate Number
            // ==========================================

            document.add(
                    new Paragraph(
                            "Certificate Number : "
                                    + certificate
                                    .getCertificateNumber()
                    )
            );

            // ==========================================
            // Issue Date
            // ==========================================

            document.add(
                    new Paragraph(
                            "Issue Date : "
                                    + certificate
                                    .getIssueDate()
                    )
            );

            // ==========================================
            // Certificate Hash
            // ==========================================

            document.add(
                    new Paragraph(
                            "Certificate Hash : "
                                    + certificate.getHash()
                    )
            );

            // ==========================================
            // Blockchain Transaction Hash
            // ==========================================

            String transactionHash =
                    certificate
                            .getBlockchainTransactionHash();

            if (transactionHash != null &&
                    !transactionHash.trim().isEmpty()) {

                document.add(
                        new Paragraph(
                                "Blockchain Transaction Hash :",
                                transactionFont
                        )
                );

                document.add(
                        new Paragraph(
                                transactionHash,
                                transactionFont
                        )
                );

            } else {

                document.add(
                        new Paragraph(
                                "Blockchain Transaction Hash : Not Available",
                                transactionFont
                        )
                );
            }

            document.add(
                    new Paragraph(" ")
            );

            // ==========================================
            // QR Code Verification URL
            // ==========================================

            String verificationUrl =
                    "http://192.168.0.106:8080/api/certificates/public/verify/"
                            + certificate.getHash();

            // ==========================================
            // Generate QR Code
            // ==========================================

            byte[] qrBytes =
                    qrCodeService.generateQRCode(
                            verificationUrl
                    );

            Image qrImage =
                    Image.getInstance(
                            qrBytes
                    );

            qrImage.scaleAbsolute(
                    120,
                    120
            );

            // ==========================================
            // Add QR Code
            // ==========================================

            document.add(
                    new Paragraph(" ")
            );

            document.add(
                    new Paragraph(
                            "Scan QR Code to Verify Certificate"
                    )
            );

            document.add(
                    qrImage
            );

            // ==========================================
            // Close PDF
            // ==========================================

            document.close();

            return out.toByteArray();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to generate PDF.",
                    e
            );
        }
    }
}