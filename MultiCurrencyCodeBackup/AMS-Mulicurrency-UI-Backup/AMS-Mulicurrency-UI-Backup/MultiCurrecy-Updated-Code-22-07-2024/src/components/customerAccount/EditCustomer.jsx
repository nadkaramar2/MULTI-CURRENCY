import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import { v4 as uuidv4 } from "uuid";
import Swal from "sweetalert2";
import { useNavigate, useParams } from "react-router-dom";
import CustomAlert from "../../layout/CustomAlert";
export default function EditCustomer() {
  const { strCustId } = useParams();
  const navigate = useNavigate();
  const [alldata1, setalldata1] = useState([]);
  const [alldata2, setalldata2] = useState([]);
  const [alldata3, setalldata3] = useState([]);
  const [alldata4, setalldata4] = useState([]);
  const [alldata5, setalldata5] = useState([]);
  const [alldata6, setalldata6] = useState([]);
  const [editCustomer, seteditCustomer] = useState([]);
  const [strAddressProofDocumentId, setStrAddressProofDocumentId] =
    useState("");
  const [strIdentityProofDocumentId, setstrIdentityProofDocumentId] =
    useState("");
  const [countrycodename, setcountrycodename] = useState("");
  const [strCountry, setstrCountry] = useState("");
  const [strState, setstrState] = useState("");
  const [strCity, setstrCity] = useState("");
  let participantID = sessionStorage.getItem("Participantid");

  const [strTitle, setstrTitle] = useState("");
  const [strFirstName, setstrFirstName] = useState("");
  const [strMiddleName, setstrMiddleName] = useState("");
  const [strLastName, setstrLastName] = useState("");
  const [strGender, setstrGender] = useState("");
  const [strDOB1, setstrDOB] = useState("");
  const [strEmailID, setstrEmailID] = useState("");
  const [strPhoneCode, setstrPhoneCode] = useState("");
  const [strMobileNo, setstrMobileNo] = useState("");
  const [strPhoneNo, setstrPhoneNo] = useState("");
  const [strAddress1, setstrAddress1] = useState("");
  const [strPinCode, setstrPinCode] = useState("");
  const [strAddressProofDocumentValue, setstrAddressProofDocumentValue] =
    useState("");
  const [strIdentityProofDocumentValue, setstrIdentityProofDocumentValue] =
    useState("");

  const [showAlert, setShowAlert] = useState(false);
  const [alertTitle, setAlertTitle] = useState("");
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");

  const handleShowSuccess = (resMessage) => {
    setAlertTitle("Alert");
    setAlertMessage(resMessage);
    setAlertType("blue");
    setShowAlert(true);
  };

  const handleShowError = (resMessage) => {
    setAlertTitle("Error");
    setAlertMessage(resMessage);
    setAlertType("red");
    setShowAlert(true);
  };

  const handleCloseAlert = () => {
    setShowAlert(false);
  };

  const showSuccess = (resMessage) => {
    Swal.fire({
      title: "Success",
      text: resMessage,
      allowOutsideClick: false,
      icon: "success",
      confirmButtonText: "OK",
    });
  };
  const showError = (resMessage) => {
    Swal.fire({
      text: resMessage,
      allowOutsideClick: false,
      icon: "error",
      title: "Oops...",
      confirmButtonText: "OK",
    });
  };

  useEffect(() => {
    addressProof();
  }, [strAddressProofDocumentId]);

  const addressProof = async () => {
    try {
      const response = await amsApi.post(
        `address_proof_document_type/documentLst`,
        {
          strParticipantId: participantID,
        }
      );
      if (response.data.code === "S0000") {
        setalldata1(response.data.addressProofDocumentTypeMasters);
      } else {
        // showError(response.data.message);
      }
    } catch (error) {}
  };
  useEffect(() => {
    identityProof();
  }, [strIdentityProofDocumentId]);
  const identityProof = async () => {
    try {
      const response = await amsApi.post(
        `identity_proof_document_type/documentLst`,
        {
          strParticipantId: participantID,
        }
      );

      if (response.data.code === "S0000") {
        setalldata2(response.data.identityProofDocumentTypeMasters);
      } else {
        // showError(response.data.message);
      }
    } catch (error) {
      // showError(error.response.data.message);
    }
  };
  useEffect(() => {
    country();
  }, [strCountry]);
  const country = async () => {
    try {
      const response = await amsApi.post(`address/getCountrylist`, {
        strParticipantID: participantID,
      });

      if (response.status === 200) {
        setalldata3(response.data.countryList);
      } else {
        // showError(response.data.message);
      }
    } catch (error) {
      // showError(error.response.data.message);
    }
  };
  useEffect(() => {
    countrycode();
  }, [countrycodename]);
  const countrycode = async () => {
    try {
      const response = await amsApi.post(`country_code_api/getContryCode`, {});

      if (response.status === 200) {
        setalldata4(response.data.countryCodeMaster);
      } else {
        // showError(response.data.message);
      }
    } catch (error) {
      // showError(error.response.data.message);
    }
  };

  const state = async (data) => {
    const demoData = alldata3.find((item) => item.countryName === data);
    const countryname = demoData ? demoData.countryId : null;
    try {
      const response = await amsApi.post(`address/getStatelist`, {
        strCountryID: countryname,
      });

      if (response.status === 200) {
        setalldata5(response.data.stateList);
      } else {
        // showError(response.data.message);
      }
    } catch (error) {
      // showError(error.response.data.message);
    }
  };
  const city = async (data) => {
    const demoData = alldata5.find((item) => item.strState === data);
    const statename = demoData ? demoData.stateId : null;
    try {
      const response = await amsApi.post(`address/getCitylist`, {
        strStateID: statename,
      });

      if (response.data.code === "S0000") {
        setalldata6(response.data.cityList);
      } else {
        // showError(response.data.message);
      }
    } catch (error) {
      // showError(error.response.data.message);
    }
  };

  useEffect(() => {
    handleSubmitView();
  }, []);

  const handleSubmitView = async (event) => {
    try {
      const response = await amsApi.post(
        `customerId/getCustomerAcountDetailsBasedOnCustId`,
        {
          strCustId: strCustId,
        }
      );
      if (response.data.code === "S0000") {
        seteditCustomer(response.data.customerIdCreationlist);
        response.data.customerIdCreationlist.map(
          (data) => (
            setstrTitle(data.strTitle),
            setstrFirstName(data.strFirstName),
            setstrLastName(data.strMiddleName),
            setstrLastName(data.strLastName),
            setstrGender(data.strGender),
            setstrDOB(data.strDOB),
            setstrEmailID(data.strEmailID),
            setstrPhoneCode(data.strPhoneCode),
            setstrAddress1(data.strAddress1),
            setstrPinCode(data.strPinCode),
            setstrCountry(data.strCountry),
            setstrState(data.strState),
            setstrCity(data.strCity),
            setstrMobileNo(data.strMobileNo),
            setStrAddressProofDocumentId(data.strAddressProofDocumentId),
            setstrAddressProofDocumentValue(data.strAddressProofDocumentValue),
            setstrIdentityProofDocumentId(data.strIdentityProofDocumentId),
            setstrIdentityProofDocumentValue(data.strIdentityProofDocumentValue)
          )
        );
      } else {
        // showError(response.data.message);
      }
    } catch (error) {
      // showError(error.response.data.message);
    }
  };

  const [countryName, setCountryName] = useState("");
  const [stateName, setStateName] = useState("");

  const handleView = async (event) => {
    if (alldata3.length === 0) {
      const countryNameObj = alldata3.find(
        (item) => item.countryId === strCountry
      );
      setCountryName(countryNameObj["countryName"]);
      handleShowError("Please Select Country Option First");
    }
    if (alldata5.length === 0) {
      const stateNameObj = alldata5.find((item) => item.stateId === strState);
      setStateName(stateNameObj["strState"]);
      handleShowError("Please Select Stste Option First");
    }
    if (alldata6.length === 0) {
      handleShowError("Please Select City Option First");
      // const cityNameObj = alldata6.find((item) => item.cityId === strCity);
      // setCityName(cityNameObj["cityName"]);
      handleShowError("Please Select City Option First");
    }
    try {
      const response = await amsApi.post(
        `customerId/updateCustomerAcountDetails`,
        {
          strParticipantID: participantID,
          strCustId: strCustId,
          strTitle: strTitle,
          strFirstName: strFirstName,
          strMiddleName: strMiddleName,
          strLastName: strLastName,
          strGender: strGender,
          strDOB: strDOB1,
          strEmailID: strEmailID,
          strPhoneCode: countrycodename,
          strMobileNo: strMobileNo,
          strPhoneNo: strPhoneNo,
          strAddress1: strAddress1,
          strPinCode: strPinCode,
          strCountry: strCountry,
          strState: strState,
          strCity: strCity,
          strAddressProofDocumentId: strAddressProofDocumentId,
          strAddressProofDocumentValue: strAddressProofDocumentValue,
          strIdentityProofDocumentType: strIdentityProofDocumentId,
          strIdentityProofDocumentValue: strIdentityProofDocumentValue,
        }
      );

      if (response.data.code === "S0000") {
        handleShowSuccess(response.data.message);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };
  return (
    <AppLayout>
      <div className="min-h-full first-line:flex items-center justify-center border-b sticky top-0">
        <div className=" bg-blue-200 rounded-md shadow-2x py-2 ">
          <h1 className="mx-auto flex justify-start px-2 text-white text-2xl font-semibold">
            <p className="flex justify-start text-sm  sm:text-xm text-black :text-2xl  leading-normal">
              Edit Customer
            </p>
          </h1>
        </div>
      </div>
      <div className="max-w-full mx-auto h-full overflow-auto max-h-full lg:max-h-[30rem]">
        <form className="">
          <div className="overflow-hidden shadow ">
            {editCustomer.map((data) => (
              <div className=" px-4  sm:p-2 white ">
                <div>
                  {""}
                  <h2 className="font-normal md:font-bold py-1">
                    Personal Information
                  </h2>
                </div>

                <div>
                  <div className="grid gap-4 mb-2 md:grid-cols-5 ">
                    <div>
                      <label
                        htmlFor="text"
                        className="block text-xs font-semibold text-gray-700"
                      >
                        Title<span className="text-red-600">*</span>
                      </label>
                      <select
                        id="strTitle"
                        name="strTitle"
                        defaultValue={data.strTitle}
                        // value={strTitle}
                        onChange={(e) => setstrTitle(e.target.value)}
                        className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      >
                        <option value="">Select</option>
                        <option value="Mr"> Mr.</option>
                        <option value="Mrs">Mrs.</option>
                      </select>
                    </div>
                  </div>
                  <div className="grid gap-4 mb-4 md:grid-cols-3 ">
                    <div>
                      <label
                        htmlFor="text"
                        className="block text-xs font-semibold text-gray-700"
                      >
                        First Name<span className="text-red-600">*</span>
                      </label>
                      <input
                        type="text"
                        id="strFirstName"
                        defaultValue={data.strFirstName}
                        onChange={(e) => setstrFirstName(e.target.value)}
                        className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        placeholder="Enter First name"
                      />
                    </div>
                    <div>
                      <label
                        htmlFor="text"
                        className="block text-xs font-semibold text-gray-700"
                      >
                        Middle Name
                      </label>
                      <input
                        type="text"
                        id="strMiddleName"
                        defaultValue={data.strMiddleName}
                        onChange={(e) => setstrMiddleName(e.target.value)}
                        className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        placeholder="Enter middle name"
                      />
                    </div>
                    <div>
                      <label
                        htmlFor="text"
                        className="block text-xs font-semibold text-gray-700"
                      >
                        Last Name<span className="text-red-600">*</span>
                      </label>
                      <input
                        type="text"
                        id="strLastName"
                        defaultValue={data.strLastName}
                        onChange={(e) => setstrLastName(e.target.value)}
                        className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        placeholder="Enter Last name"
                      />
                    </div>
                    <div>
                      <label
                        htmlFor="text"
                        className="block text-xs font-semibold text-gray-700"
                      >
                        Gender<span className="text-red-600">*</span>
                      </label>
                      <select
                        id="strGender"
                        name="strGender"
                        defaultValue={data.strGender}
                        onChange={(e) => setstrGender(e.target.value)}
                        className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      >
                        <option value=""> Select </option>
                        <option value="Male">Male</option>
                        <option value="Female">Female</option>
                      </select>
                    </div>
                    <div>
                      <label
                        htmlFor="text"
                        className="block text-xs font-semibold text-gray-700"
                      >
                        Date Of Birth<span className="text-red-600">*</span>
                      </label>
                      <input
                        type="text"
                        id="birthDate"
                        defaultValue={data.strDOB}
                        onChange={(e) => setstrDOB(e.target.value)}
                        className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        placeholder="eg.12/08/2000"
                      />
                    </div>
                    <div>
                      <label
                        htmlFor="text"
                        className="block text-xs font-semibold text-gray-700"
                      >
                        Email Id<span className="text-red-600">*</span>
                      </label>
                      <input
                        type="text"
                        id="strEmailID"
                        defaultValue={data.strEmailID}
                        onChange={(e) => setstrEmailID(e.target.value)}
                        className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        placeholder="Enter mail Id"
                      />
                    </div>
                    <div>
                      <label
                        htmlFor="text"
                        className="block text-xs font-semibold text-gray-700"
                      >
                        Country Code<span className="text-red-600">*</span>
                      </label>
                      <select
                        id="countrycodename"
                        name="countrycodename"
                        value={countrycodename}
                        onChange={(e) => setcountrycodename(e.target.value)}
                        className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      >
                        <option value="">Select</option>
                        {alldata4.map((data) => (
                          <optgroup kay={uuidv4()}>
                            <option
                              className="capatlize text-lg"
                              value={data.phonecode}
                            >
                              {data.country_code}
                            </option>
                          </optgroup>
                        ))}
                      </select>
                    </div>
                    <div>
                      <label
                        htmlFor="text"
                        className="block text-xs font-semibold text-gray-700"
                      >
                        Mobile Number<span className="text-red-600">*</span>
                      </label>
                      <input
                        type="text"
                        id="strMobileNo"
                        defaultValue={data.strMobileNo}
                        onChange={(e) => setstrMobileNo(e.target.value)}
                        className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        placeholder="Enter Mobile number"
                      />
                    </div>
                    <div>
                      <label
                        htmlFor="text"
                        className="block text-xs font-semibold text-gray-700"
                      >
                        Phone Number
                      </label>
                      <input
                        type="text"
                        id="phone"
                        defaultValue={data.strPhoneNo}
                        onChange={(e) => setstrPhoneNo(e.target.value)}
                        className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        placeholder="Enter Phone number"
                      />
                    </div>
                  </div>
                  <div>
                    {""}
                    <h2 className="font-normal md:font-bold py-1">
                      Address Information
                    </h2>
                  </div>
                  <div className="grid gap-4 mb-4 md:grid-cols-3 ">
                    <div>
                      <label
                        htmlFor="text"
                        className="block text-xs font-semibold text-gray-700"
                      >
                        Address Line1<span className="text-red-600">*</span>
                      </label>
                      <input
                        type="text"
                        id="strAddress1"
                        defaultValue={data.strAddress1}
                        onChange={(e) => setstrAddress1(e.target.value)}
                        className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        placeholder="Enter Address Line1"
                      />
                    </div>
                    <div>
                      <label
                        htmlFor="text"
                        className="block text-xs font-semibold text-gray-700"
                      >
                        Address Line2
                      </label>
                      <input
                        type="text"
                        id="address2"
                        defaultValue={data.address2}
                        // onChange={(e) => settransactionData1(e.target.value)}
                        className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        placeholder="Enter Address Line2"
                      />
                    </div>

                    <div>
                      <label
                        htmlFor="text"
                        className="block text-xs font-semibold text-gray-700"
                      >
                        Address Line3
                      </label>
                      <input
                        type="text"
                        id="address3"
                        defaultValue={data.address3}
                        // onChange={(e) => setaddress3(e.target.value)}
                        className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        placeholder="Enter Address Line3"
                      />
                    </div>
                  </div>
                  <div className="grid gap-4 mb-4 md:grid-cols-3 ">
                    <div>
                      <label
                        htmlFor="text"
                        className="block text-xs font-semibold text-gray-700"
                      >
                        Country<span className="text-red-600">*</span>
                      </label>
                      <select
                        name="strCountry"
                        id="strCountry"
                        defaultValue={data.strCountry}
                        value={strCountry}
                        onChange={(e) =>
                          setstrCountry(e.target.value, state(e.target.value))
                        }
                        className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      >
                        <option value="">Select</option>
                        {alldata3.map((data) => (
                          <option
                            className="capatlize text-sm"
                            value={data.countryName}
                            //onChange={setstrState(data.countryId)}
                          >
                            {data.countryName}
                          </option>
                        ))}
                      </select>
                    </div>
                    <div>
                      <label
                        htmlFor="text"
                        className="block text-xs font-semibold text-gray-700"
                      >
                        State<span className="text-red-600">*</span>
                      </label>
                      <select
                        id="strState"
                        name="strState"
                        defaultValue={data.strState}
                        value={strState}
                        onChange={(e) =>
                          setstrState(e.target.value, city(e.target.value))
                        }
                        className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      >
                        <option value="">Select</option>
                        {alldata5.map((data) => (
                          <option
                            className="capatlize text-sm"
                            value={data.strState}
                          >
                            {data.strState}
                          </option>
                        ))}
                      </select>
                    </div>
                    <div>
                      <label
                        htmlFor="text"
                        className="block text-xs font-semibold text-gray-700"
                      >
                        City<span className="text-red-600">*</span>
                      </label>
                      <select
                        id="strCity"
                        name="strCity"
                        defaultValue={data.strCity}
                        value={strCity}
                        onChange={(e) => setstrCity(e.target.value)}
                        className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      >
                        <option value="">Select</option>
                        {alldata6.map((data) => (
                          <option
                            className="capatlize text-sm"
                            value={data.cityName}
                          >
                            {data.cityName}
                          </option>
                        ))}
                      </select>
                    </div>
                    <div>
                      <label
                        htmlFor="text"
                        className="block text-xs font-semibold text-gray-700"
                      >
                        Postal Code<span className="text-red-600">*</span>
                      </label>
                      <input
                        type="text"
                        id="strPinCode"
                        defaultValue={data.strPinCode}
                        // onChange={(e) => settransactionData1(e.target.value)}
                        className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        placeholder="Enter Postal Code"
                      />
                    </div>
                  </div>
                  <div>
                    {""}
                    <h2 className="font-normal md:font-bold py-1">
                      Address Proof(POA)
                    </h2>
                  </div>
                  <div className="grid gap-4 mb-4 md:grid-cols-2 ">
                    <div>
                      <label
                        htmlFor="text"
                        className="block text-xs font-semibold text-gray-700"
                      >
                        Document Type<span className="text-red-600">*</span>
                      </label>
                      <select
                        name="strAddressProofDocumentId"
                        id="strAddressProofDocumentId"
                        value={strAddressProofDocumentId}
                        onChange={(e) =>
                          setStrAddressProofDocumentId(e.target.value)
                        }
                        className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      >
                        <option value="">Select</option>
                        {alldata1.map((data) => (
                          <option
                            className="capatlize text-sm"
                            // value={}
                          >
                            {data.strDocumentDescr}
                          </option>
                        ))}
                      </select>
                    </div>
                    <div>
                      <label
                        htmlFor="text"
                        className="block text-xs font-semibold text-gray-700"
                      >
                        Document Value
                        <span className="text-red-600">*</span>
                      </label>
                      <input
                        type="text"
                        id="strAddressProofDocumentValue"
                        defaultValue={data.strAddressProofDocumentValue}
                        onChange={(e) =>
                          setstrAddressProofDocumentValue(e.target.value)
                        }
                        className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        placeholder="Enter Document value"
                      />
                    </div>
                  </div>
                  <div>
                    {""}
                    <h2 className="font-normal md:font-bold">
                      Identification Proof(POI)
                    </h2>
                    <br />
                  </div>
                  <div className="grid gap-6 mb-6 md:grid-cols-2 ">
                    <div>
                      <label
                        htmlFor="text"
                        className="block text-xs font-semibold text-gray-700"
                      >
                        Document Type<span className="text-red-600">*</span>
                      </label>
                      <select
                        name="strIdentityProofDocumentId"
                        id="strIdentityProofDocumentId"
                        value={strIdentityProofDocumentId}
                        onChange={(e) =>
                          setstrIdentityProofDocumentId(e.target.value)
                        }
                        className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      >
                        <option value="">Select</option>
                        {alldata2.map((data) => (
                          <option
                            className="capatlize text-sm"
                            // value={}
                          >
                            {data.strDocumentDescr}
                          </option>
                        ))}
                      </select>
                    </div>
                    <div>
                      <label
                        htmlFor="text"
                        className="block text-xs font-semibold text-gray-700"
                      >
                        Document Value
                        <span className="text-red-600">*</span>
                      </label>
                      <input
                        type="text"
                        id="strIdentityProofDocumentValue"
                        defaultValue={data.strIdentityProofDocumentValue}
                        onChange={(e) =>
                          setstrIdentityProofDocumentValue(e.target.value)
                        }
                        className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        placeholder="Enter Document value"
                      />
                    </div>
                  </div>
                </div>
              </div>
            ))}
            <div className="bg-gray-100 px-4 py-1 text-right sm:px-3 ">
              <button
                title="Click Submit Button"
                type="button"
                data-modal-toggle="defaultModal"
                onClick={handleView}
                className="inline-flex justify-center rounded-md border border-transparent bg-blue-500 py-1.5 px-3  text-xs font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
              >
                Submit
              </button>
              <button
                title="Clear Data"
                type="button"
                // onClick={handleClick}
                className="inline-flex justify-center rounded-md border border-transparent bg-rose-500 py-2 px-4 mx-4 text-xs font-medium text-white shadow-sm hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
              >
                Clear
              </button>
              <button
                title="Go Back"
                type="button"
                onClick={() => navigate(-1)}
                className="inline-flex justify-center rounded-md border border-transparent bg-blue-500 py-1.5 px-3 mx-4 text-xs font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
              >
                BACK
              </button>
            </div>
          </div>
        </form>
      </div>
      {/* Your existing code here */}

      {showAlert && (
        <CustomAlert
          title={alertTitle}
          message={alertMessage}
          type={alertType}
          onClose={handleCloseAlert}
        />
      )}
    </AppLayout>
  );
}
