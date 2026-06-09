import React, { useState, useEffect, Fragment } from "react";
import amsApi from "../../api/amsApi";
import AppLayout from "../../layout/AppLayout";
import CustomAlert from "../../layout/CustomAlert";
import swal from "sweetalert";
import AutoPagintation from "../../UI/AutoPagintation";
export default function ChargingConfigu() {
  const [accountType, setAccountType] = useState("");
  const [alldata, setAlldata] = useState([]);
  let participantID = sessionStorage.getItem("Participantid");
  const [search, setSearch] = useState("");
  const [searchQuery, setSearchQuery] = useState("");
  const [searchQuery1, setSearchQuery1] = useState("");
  const [searchQuery2, setSearchQuery2] = useState("");
  const [checkpoint, setCheckpoint] = useState(0);
  const [checkpoint1, setCheckpoint1] = useState(0);
  const [checkpoint2, setCheckpoint2] = useState(0);
  const [checkpoint3, setCheckpoint3] = useState(0);
  const [error, setError] = useState("");

  const [currentPage, setCurrentPage] = useState(1);
  const [pagePerData, setPagePerData] = useState(10);
  const [currentPage1, setCurrentPage1] = useState(1);
  const [currentPage2, setCurrentPage2] = useState(1);
  const [pagePerData2, setPagePerData2] = useState(10);
  const [pagePerData1, setPagePerData1] = useState(10);

  //   Pagination
  const [itemPage, setItemPage] = useState(10);
  const [tcount, setTcount] = useState(0);
  //   Pagination
  const [itemPage1, setItemPage1] = useState(10);
  const [tcount1, setTcount1] = useState(0);
  //   Pagination
  const [itemPage2, setItemPage2] = useState(10);
  const [tcount2, setTcount2] = useState(0);

  let userid = localStorage.getItem("userName");

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

  useEffect(() => {
    AccountType();
  }, []);

  const AccountType = async () => {
    try {
      const response = await amsApi.get(`/accountType/getAccntType`, {
        headers: {
          "Content-Type": "application/json",
        },
      });
      if (response.data.code === "S0000") {
        setAlldata(response.data.accountTypeMasterlistData);
      } else {
        // handleShowError(response.data.message);
      }
    } catch (error) {}
  };

  //   1
  const [rows, setRows] = useState([]);

  const cardrelated = async () => {
    try {
      const response = await amsApi.post(
        `getChargeMasterList/getChargeMasterList/pagination/${pagePerData}/${currentPage}`,
        {}
      );
      if (response.status === 200) {
        setRows(response.data.chargeMasters);
        const values = [...rows];
        for (let index = 0; index < values.length; index++) {
          values[index].isDisabled = false;

          values[index].strAmount = "-";

          setRows(values);
        }
        setCheckpoint1(1);
        // showSuccess(response.data.message);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error);
    }
  };

  // for charge related
  const handleCheckboxChange = (event, strChargeType) => {
    const values = [...rows];

    const index = values.findIndex(
      (row) => row.strChargeType === strChargeType
    );
    values[index].isDisabled = event.target.checked;
    setRows(values);
    console.log(values);
  };

  const handleInputChange = (event, strChargeType) => {
    const values = [...rows];
    const index = values.findIndex(
      (row) => row.strChargeType === strChargeType
    );
    values[index].strAmount = event.target.value;

    setRows(values);
  };

  // Transaction Related
  const [list1, setList1] = useState([]);
  const TransactionCharg = async () => {
    try {
      const response = await amsApi.post(
        `getChargeMasterList/getTransactionChargList/pagination/${pagePerData1}/${currentPage1}`,
        {}
      );
      if (response.status === 200) {
        setList1(response.data.chargeMasters);
        const values = [...list1];
        for (let index = 0; index < values.length; index++) {
          values[index].isDisabled = false;

          values[index].strAmount = "-";

          setList1(values);
        }
        setCheckpoint2(1);
        // showSuccess(response.data.message);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error);
    }
  };

  const handleCheckboxChange1 = (event, strChargeType) => {
    const values = [...list1];

    const index = values.findIndex(
      (data) => data.strChargeType === strChargeType
    );
    values[index].isDisabled = event.target.checked;
    setList1(values);
  };

  const handleInputChange1 = (event, strChargeType) => {
    const values = [...list1];
    const index = values.findIndex(
      (data) => data.strChargeType === strChargeType
    );
    values[index].strAmount = event.target.value;
    setList1(values);
  };

  // Fuel charge
  const [list2, setList2] = useState([]);
  const FuelCharg = async () => {
    try {
      const response = await amsApi.post(
        `getChargeMasterList/getFuelChargList/pagination/${pagePerData2}/${currentPage2}`,
        {}
      );
      if (response.status === 200) {
        setList2(response.data.chargeMasters);
        const values = [...list2];

        for (let index = 0; index < values.length; index++) {
          values[index].isDisabled = false;

          values[index].strPercentage = "-";

          setList2(values);
        }
        setCheckpoint3(1);
        // showSuccess(response.data.message);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error);
    }
  };

  const handleCheckboxChange2 = (event, strChargeType) => {
    const values = [...list2];
    const index = values.findIndex(
      (value) => value.strChargeType === strChargeType
    );
    values[index].isDisabled = event.target.checked;
    setList2(values);
  };

  const handleInputChange2 = (event, strChargeType) => {
    const values = [...list2];

    const index = values.findIndex(
      (value) => value.strChargeType === strChargeType
    );
    values[index].strPercentage = event.target.value;
    setList2(values);
  };
  // save button

  const handleSubmit = async (event) => {
    event.preventDefault();

    let arr = [];

    for (let index = 0; index < rows.length; index++) {
      if (rows[index].isDisabled === true) {
        arr.push(rows[index]);
      }
    }

    for (let index = 0; index < list1.length; index++) {
      if (list1[index].isDisabled === true) {
        arr.push(list1[index]);
      }
    }

    for (let index = 0; index < list2.length; index++) {
      if (list2[index].isDisabled === true) {
        arr.push(list2[index]);
      }
    }

    let resArr = [];
    console.log(resArr);
    for (let i = 0; i < arr.length; i++) {
      resArr.push({
        strParticipantID: participantID,
        strCreatedBy: userid,
        strChargeDescription: arr[i].strChargeDescription,
        strAccountType: accountType,
        strChargeType: arr[i].strChargeType,
        strAmount: arr[i].strAmount,
        strPercentage: arr[i].strPercentage,
      });
    }
    if (!accountType) {
      swal("Please Select  your accountType and Charges  ");
    } else {
      try {
        const response = await amsApi.post(`getChargeMasterList/addConfig`, {
          AccountTypeChargesBulkData: resArr,
        });
        if (response.data.code === "S0000") {
          handleShowSuccess(response.data.message);
          handleClick();
        } else {
          handleShowError(response.data.message);
        }
      } catch (error) {
        handleShowError(
          "Sorry, the server is under mentaines. Please try again later"
        );
      }
    }
  };

  // Already Added Charge list

  // useEffect(() => {
  //   AlreadyCharges();
  // }, []);
  const handleAccountTypeChange = (e) => {
    const selectedAccountType = e.target.value;
    setAccountType(selectedAccountType);
    AlreadyCharges(selectedAccountType);
    cardrelated();
    TransactionCharg();
    FuelCharg();
  };

  const [viewalreadycharge, setViewalreadycharge] = useState(false);

  const [fetch, setFetch] = useState([]);
  const AlreadyCharges = async (selectedAccountType) => {
    setViewalreadycharge(true);
    if (selectedAccountType === "") {
      setError(true);
    } else {
      try {
        const response = await amsApi.post(
          `accountTypeCharges/getSelectedChargesAcountTypeWise`,
          {
            strAccountType: selectedAccountType,
          }
        );
        if (response.data.code === "S0000") {
          setFetch(response.data.accountTypeChargesList);
          setCheckpoint(1);

          // showSuccess(response.data.message);
        } else {
          // handleShowError(response.data.message);
        }
      } catch (error) {
        // handleShowError(error);
      }
    }
  };
  const handleClick = () => {
    setAccountType("");
    setRows([]);
    setList1([]);
    setList2([]);
  };

  // Paginations for charge related
  const [page, setPage] = useState("");
  const [frompage, setFromPage] = useState("");
  const [topage, setToPage] = useState("");

  const itemPerPageHandler = (e) => {
    setPage(e.target.value);
    if (e.target.value === "custom") {
      return null;
    }
    setPagePerData(parseInt(e.target.value));
    setCurrentPage(1);
  };

  const customItemPerPageHandler = () => {
    setPagePerData(parseInt(topage));
    setCurrentPage(frompage);
  };

  const TotalPageNo = Math
    .floor
    // parseInt(filteredData[0].tcount) / parseInt(pagePerData) + 1
    ();

  //   const itemPerPageHandler = (e) => {
  //     props.addPageperData(parseInt(e.target.value));
  //     props.addCurrentPage(1);
  //   };
  function nextpage() {
    if (TotalPageNo > currentPage) {
      setCurrentPage(currentPage + 1);
    }
  }
  function prevPage() {
    if (currentPage > 1) {
      setCurrentPage(currentPage - 1);
    }
  }

  // Paginations for Transaction related
  const [page1, setPage1] = useState("");
  const [frompage1, setFromPage1] = useState("");
  const [topage1, setToPage1] = useState("");

  const itemPerPageHandler1 = (e) => {
    setPage1(e.target.value);
    if (e.target.value === "custom") {
      return null;
    }
    setPagePerData1(parseInt(e.target.value));
    setCurrentPage1(1);
  };

  const customItemPerPageHandler1 = () => {
    setPagePerData1(parseInt(topage));
    setCurrentPage1(frompage);
  };

  const TotalPageNo1 = Math
    .floor
    // parseInt(filteredData[0].tcount) / parseInt(pagePerData) + 1
    ();

  //   const itemPerPageHandler = (e) => {
  //     props.addPageperData(parseInt(e.target.value));
  //     props.addCurrentPage(1);
  //   };
  function nextpage1() {
    if (TotalPageNo > currentPage) {
      setCurrentPage1(currentPage + 1);
    }
  }
  function prevPage1() {
    if (currentPage > 1) {
      setCurrentPage1(currentPage - 1);
    }
  }

  // fuel related charges
  // Paginations for Transaction related
  const [page2, setPage2] = useState("");
  const [frompage2, setFromPage2] = useState("");
  const [topage2, setToPage2] = useState("");

  const itemPerPageHandler2 = (e) => {
    setPage2(e.target.value);
    if (e.target.value === "custom") {
      return null;
    }
    setPagePerData2(parseInt(e.target.value));
    setCurrentPage2(1);
  };

  const customItemPerPageHandler2 = () => {
    setPagePerData2(parseInt(topage));
    setCurrentPage2(frompage);
  };

  const TotalPageNo2 = Math
    .floor
    // parseInt(filteredData[0].tcount) / parseInt(pagePerData) + 1
    ();

  //   const itemPerPageHandler = (e) => {
  //     props.addPageperData(parseInt(e.target.value));
  //     props.addCurrentPage(1);
  //   };
  function nextpage2() {
    if (TotalPageNo > currentPage2) {
      setCurrentPage2(currentPage2 + 1);
    }
  }
  function prevPage2() {
    if (currentPage2 > 1) {
      setCurrentPage2(currentPage2 - 1);
    }
  }

  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full">
        <div className="w-full shadow-md mt-2 ">
          <div className="px-4 sm:px-10  bg-blue-200 ">
            <div className=" flex  items-center justify-between">
              <p className="text-base sm:text-xm text-black :text-2xl  leading-normal ">
                Charging Configuration
              </p>
            </div>
          </div>
          <div className=" md:col-span-1 lg:col-span-1  bg-blue-200">
            <form className="">
              <div className="overflow-hidden shadow ">
                <div className=" px-4 sm:p-1 bg-white  ">
                  <div className=" justify-between grid gap-6 md:grid-cols-3 px-8">
                    <div>
                      <label
                        htmlFor="accountType"
                        className="block text-xs font-semibold text-gray-700"
                      >
                        Account Type
                        <span className="text-red-600 px-2">*</span>
                      </label>
                      <select
                        name="accountType"
                        id="accountType"
                        value={accountType}
                        onChange={handleAccountTypeChange}
                        className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      >
                        <option value="">Select</option>
                        {alldata.map((data) => (
                          // <optgroup key={uuidv4()}>
                          <option
                            className="capatlize text-sm"
                            value={data.strAccountType}
                          >
                            {data.strAccountType || "-"} -{""}
                            {data.strDescription || "-"}
                          </option>
                          // </optgroup>
                        ))}
                      </select>
                      {error && accountType.length <= 0 ? (
                        <p className="text-red-500   text-xs font-normal">
                          Please select Account Type
                        </p>
                      ) : (
                        ""
                      )}
                    </div>
                  </div>
                </div>
              </div>
            </form>
          </div>

          {viewalreadycharge === true && (
            <>
              <div className="flex justify-between items-center sm:px-3  lg:px-8 bg-blue-200  mt-1 rounded-t-sm ">
                <p className="text-xs  sm:text-xs text-black  leading-normal ">
                  {""}
                  ALREADY SELECTED CHARGES LIST
                </p>
                <div className=" flex justify-center px-2 mx-2    ">
                  <div className="pt-1 relative mx-auto text-gray-600">
                    <input
                      title="Search Data"
                      type="search"
                      id="search"
                      className=" block  text-gray-700 text-xs bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 px-2 py-1"
                      placeholder="Search... "
                      value={search}
                      onChange={(e) => setSearch(e.target.value)}
                      style={{
                        backgroundColor:
                          checkpoint === 1 ? "white" : "lightgray",
                      }}
                      disabled={checkpoint !== 1}
                    />
                    <button
                      type="submit"
                      className="absolute right-0 top-0 mt-2 mr-2"
                    >
                      <svg
                        xmlns="http://www.w3.org/2000/svg"
                        fill="none"
                        viewBox="0 0 24 24"
                        strokeWidth={1.5}
                        stroke="currentColor"
                        className="w-6 h-6"
                      >
                        <path
                          strokeLinecap="round"
                          strokeLinejoin="round"
                          d="M21 21l-5.197-5.197m0 0A7.5 7.5 0 105.196 5.196a7.5 7.5 0 0010.607 10.607z"
                        />
                      </svg>
                    </button>
                  </div>
                </div>
              </div>

              {/* table */}

              <div className="overflow-x-auto relative shadow-md  ">
                <div className="table-wrp block max-h-[18rem] ">
                  <table className="w-full text-xs text-left text-black dark:text-blue-100">
                    <thead className=" border-b text-xs sticky top-0 text-gray-500 uppercase bg-gray-100 dark:text-white">
                      <tr>
                        <th scope="col" className="py-1 px-6">
                          charge type
                        </th>
                        <th scope="col" className="py-1 px-6">
                          charge description
                        </th>
                        <th scope="col" className="py-1 px-6">
                          amount
                        </th>
                        <th scope="col" className="py-1 px-6">
                          Percentage
                        </th>
                      </tr>
                    </thead>
                    <tbody>
                      {fetch.length > 0 ? (
                        <>
                          {fetch
                            .filter((index) =>
                              index.strChargeType
                                .toLowerCase()
                                .includes(search.toLowerCase())
                            )
                            .map((i) => (
                              <tr className=" border-b dark:border-neutral-500">
                                <td className="px-6 py-1 whitespace-nowrap">
                                  <div className="text-xs text-gray-900">
                                    {i.strChargeType}
                                  </div>
                                </td>
                                <td className="px-6 py-1 whitespace-nowrap">
                                  <div className="text-xs  text-gray-900">
                                    {i.strChargeDescription}
                                  </div>
                                </td>
                                <td className="px-6 py-1 whitespace-nowrap text-center">
                                  <div className="text-xs  text-gray-900 ">
                                    {i.strAmount}
                                  </div>
                                </td>
                                <td className="px-6 py-1 whitespace-nowrap">
                                  <div className="text-xs  text-gray-900">
                                    {i.strPercentage}
                                  </div>
                                </td>
                              </tr>
                            ))}
                        </>
                      ) : (
                        <tr>
                          <td colSpan="6" className="px-6 py-1 text-center">
                            <span className="text-xs font-normal text-gray-500">
                              No data available.
                            </span>
                          </td>
                        </tr>
                      )}
                    </tbody>
                  </table>
                </div>
              </div>
            </>
          )}
        </div>
        <div className="max-w-7xl mx-auto h-full overflow-auto max-h-full lg:max-h-[19rem]">
          <div className="flex justify-between items-center sm:px-6 lg:px-8 bg-blue-200  mt-1 rounded-t-lg ">
            <p className="text-sm  sm:text-sm text-black  leading-normal ">
              {""}
              CARD RELATED CHARGES
            </p>

            <div className=" flex justify-center px-2 mx-2 font-medium    ">
              <div className="pt-1 relative mx-auto text-gray-600">
                <input
                  title="Search Data"
                  type="search"
                  id="search"
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  className=" block font-normal text-gray-700 text-sm bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 px-2 py-1"
                  placeholder="Search... "
                  style={{
                    backgroundColor: checkpoint1 === 1 ? "white" : "lightgray",
                  }}
                  disabled={checkpoint1 !== 1}
                />
                <button
                  type="submit"
                  className="absolute right-0 top-0 mt-3 mr-2"
                >
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    fill="none"
                    viewBox="0 0 24 24"
                    strokeWidth={1.5}
                    stroke="currentColor"
                    className="w-6 h-6"
                  >
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      d="M21 21l-5.197-5.197m0 0A7.5 7.5 0 105.196 5.196a7.5 7.5 0 0010.607 10.607z"
                    />
                  </svg>
                </button>
              </div>
            </div>
          </div>
          <AutoPagintation
            addPageperData={setItemPage}
            addCurrentPage={setTcount}
            pagePerData={itemPage}
            currentPage={tcount}
            tcount={rows[0]?.strTotalCount}
            reCallApi={cardrelated}
          />

          {/* table */}

          <div className="overflow-x-auto relative shadow-md ">
            <div className="table-wrp block max-h-[27rem] ">
              <table className="w-full text-xs text-left text-black dark:text-blue-100">
                <thead className=" border-b text-xs sticky top-0 text-gray-500  bg-gray-100 dark:text-white">
                  <tr>
                    <th scope="col" className="px-6 py-1"></th>
                    <th scope="col" className="py-1 px-6">
                      Charge Type
                    </th>
                    <th scope="col" className="py-1 px-6">
                      Charge Description
                    </th>
                    <th scope="col" className="py-1 px-6">
                      Amount
                    </th>
                  </tr>
                </thead>
                <tbody>
                  {rows.length > 0 ? (
                    <>
                      {rows
                        .filter(
                          (row) =>
                            row.strChargeType
                              .toLowerCase()
                              .includes(searchQuery.toLowerCase()) ||
                            row.strChargeDescription
                              .toLowerCase()
                              .includes(searchQuery.toLowerCase())
                        )
                        .map((row) => (
                          <tr className=" border-b dark:border-neutral-500">
                            <td className="px-6 py-1 whitespace-nowrap">
                              <div className="form-control">
                                <label className="cursor-pointer label">
                                  <input
                                    type="checkbox"
                                    checked={row.isDisabled}
                                    onChange={(event) =>
                                      handleCheckboxChange(
                                        event,
                                        row.strChargeType
                                      )
                                    }
                                    className="checkbox checkbox-info  checkbox-sm"
                                  />
                                </label>
                              </div>
                            </td>
                            <td className="px-6 py-1 whitespace-nowrap">
                              <div className="text-xs text-gray-900">
                                {row.strChargeType}
                              </div>
                            </td>
                            <td className="px-6 py-1 whitespace-nowrap">
                              <div className="text-xs  text-gray-900">
                                {row.strChargeDescription}
                              </div>
                            </td>
                            <td>
                              <input
                                type="text"
                                name="amount"
                                value={row.amount}
                                onChange={(event) =>
                                  handleInputChange(event, row.strChargeType)
                                }
                                disabled={!row.isDisabled}
                                className="block w-44 px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                              />
                            </td>
                          </tr>
                        ))}
                    </>
                  ) : (
                    <tr>
                      <td colSpan="6" className="px-6 py-1 text-center">
                        <span className="text-sm font-normal text-gray-500">
                          No data available.
                        </span>
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>
          </div>

          {/* Transfer  */}

          <div className="flex justify-between items-center sm:px-6 lg:px-8 bg-blue-300   mt-1 rounded-t-sm ">
            <p className="text-sm  sm:text-sm text-black  leading-normal ">
              {""}
              TRANSACTION RELATED CHARGES
            </p>
            <div className=" flex justify-center px-2 mx-2 font-sm    ">
              <div className="pt-2 relative mx-auto text-gray-600">
                <input
                  title="Search Data"
                  type="search"
                  id="search"
                  className=" block font-normal text-gray-700 text-sm bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 px-2 py-1"
                  placeholder="Search... "
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  style={{
                    backgroundColor: checkpoint2 === 1 ? "white" : "lightgray",
                  }}
                  disabled={checkpoint2 !== 1}
                />
                <button
                  type="submit"
                  className="absolute right-0 top-0 mt-2 mr-2"
                >
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    fill="none"
                    viewBox="0 0 24 24"
                    strokeWidth={1.5}
                    stroke="currentColor"
                    className="w-6 h-6"
                  >
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      d="M21 21l-5.197-5.197m0 0A7.5 7.5 0 105.196 5.196a7.5 7.5 0 0010.607 10.607z"
                    />
                  </svg>
                </button>
              </div>
            </div>
          </div>

          <AutoPagintation
            addPageperData={setItemPage1}
            addCurrentPage={setTcount1}
            pagePerData={itemPage1}
            currentPage={tcount1}
            tcount={list1[0]?.strTotalCount}
            reCallApi={TransactionCharg}
          />

          <div className="overflow-x-auto relative shadow-md ">
            <div className="table-wrp block max-h-[27rem] ">
              <table className="w-full text-xs text-left text-black dark:text-blue-100">
                <thead className=" border-b text-xs sticky top-0 text-gray-500  bg-gray-100 dark:text-white">
                  <tr>
                    <th scope="col" className="px-6 py-1"></th>
                    <th scope="col" className="py-1 px-6">
                      Charge Type
                    </th>
                    <th scope="col" className="py-1 px-6">
                      Charge Description
                    </th>
                    <th scope="col" className="py-1 px-6">
                      Amount
                    </th>
                  </tr>
                </thead>

                <tbody>
                  {list1.length > 0 ? (
                    <>
                      {list1
                        .filter(
                          (data) =>
                            data.strChargeType
                              .toLowerCase()
                              .includes(searchQuery1.toLowerCase()) ||
                            data.strChargeDescription
                              .toLowerCase()
                              .includes(searchQuery1.toLowerCase())
                        )
                        .map((data) => (
                          <tr className=" border-b dark:border-neutral-500">
                            <td className="px-6 py-1 whitespace-nowrap">
                              <div className="form-control">
                                <label className="cursor-pointer label">
                                  <input
                                    type="checkbox"
                                    checked={data.isDisabled}
                                    onChange={(event) =>
                                      handleCheckboxChange1(
                                        event,
                                        data.strChargeType
                                      )
                                    }
                                    className="checkbox checkbox-info  checkbox-sm"
                                  />
                                </label>
                              </div>
                            </td>
                            <td className="px-6 py-1 whitespace-nowrap">
                              <div className="text-xs  text-gray-900">
                                {data.strChargeType}
                              </div>
                            </td>
                            <td className="px-6 py-1 whitespace-nowrap">
                              <div className="text-xs  text-gray-900">
                                {data.strChargeDescription}
                              </div>
                            </td>
                            <td>
                              <input
                                type="text"
                                name="amount"
                                value={data.amount}
                                onChange={(event) =>
                                  handleInputChange1(event, data.strChargeType)
                                }
                                disabled={!data.isDisabled}
                                className="block w-44 px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                              />
                            </td>
                          </tr>
                        ))}
                    </>
                  ) : (
                    <tr>
                      <td colSpan="6" className="px-6 py-1 text-center">
                        <span className="text-sm font-sm text-gray-500">
                          No data available.
                        </span>
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>
          </div>
          {/* Fule charge */}

          <div className="flex justify-between items-center sm:px-6 lg:px-8 bg-blue-300  mt-1 rounded-t-lg ">
            <p className="text-sm  sm:text-sm text-black  leading-normal ">
              {""}
              FUEL RELATED CHARGES
            </p>
            <div className=" flex justify-center px-2 mx-2 font-sm    ">
              <div className="pt-1 relative mx-auto text-gray-600">
                <input
                  title="Search Data"
                  type="search"
                  id="search"
                  className=" block font-normal text-gray-700 text-sm bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 px-2 py-1"
                  placeholder="Search... "
                  value={searchQuery2}
                  onChange={(e) => setSearchQuery2(e.target.value)}
                  style={{
                    backgroundColor: checkpoint3 === 1 ? "white" : "lightgray",
                  }}
                  disabled={checkpoint3 !== 1}
                />
                <button
                  type="submit"
                  className="absolute right-0 top-0 mt-3 mr-2"
                >
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    fill="none"
                    viewBox="0 0 24 24"
                    strokeWidth={1.5}
                    stroke="currentColor"
                    className="w-4 h-4"
                  >
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      d="M21 21l-5.197-5.197m0 0A7.5 7.5 0 105.196 5.196a7.5 7.5 0 0010.607 10.607z"
                    />
                  </svg>
                </button>
              </div>
            </div>
          </div>
          <AutoPagintation
            addPageperData={setItemPage2}
            addCurrentPage={setTcount2}
            pagePerData={itemPage2}
            currentPage={tcount2}
            tcount={list2[0]?.strTotalCount}
            reCallApi={FuelCharg}
          />

          <div className="overflow-x-auto relative shadow-md  ">
            <div className="table-wrp block max-h-[27rem] ">
              <table className="w-full text-xs text-left text-black dark:text-blue-100">
                <thead className=" border-b text-xs sticky top-0 text-gray-500  bg-gray-100 dark:text-white">
                  <tr>
                    <th scope="col" className="px-6 py-1"></th>
                    <th scope="col" className="py-1 px-6">
                      Charge Type
                    </th>
                    <th scope="col" className="py-1 px-6">
                      Charge Description
                    </th>
                    <th scope="col" className="py-1 px-6">
                      Percentage
                    </th>
                  </tr>
                </thead>

                <tbody>
                  {list2.length > 0 ? (
                    <>
                      {list2
                        .filter(
                          (value) =>
                            value.strChargeType
                              .toLowerCase()
                              .includes(searchQuery2.toLowerCase()) ||
                            value.strChargeDescription
                              .toLowerCase()
                              .includes(searchQuery2.toLowerCase())
                        )
                        .map((value) => (
                          <tr className=" border-b dark:border-neutral-500">
                            <td className="px-6 py-1 whitespace-nowrap">
                              <div className="form-control">
                                <label className="cursor-pointer label">
                                  <input
                                    type="checkbox"
                                    checked={value.isDisabled}
                                    onChange={(event) =>
                                      handleCheckboxChange2(
                                        event,
                                        value.strChargeType
                                      )
                                    }
                                    className="checkbox checkbox-info  checkbox-sm"
                                  />
                                </label>
                              </div>
                            </td>
                            <td className="px-6 py-1 whitespace-nowrap">
                              <div className="text-xs  text-gray-900">
                                {value.strChargeType}
                              </div>
                            </td>
                            <td className="px-6 py-1 whitespace-nowrap">
                              <div className="text-xs  text-gray-900">
                                {value.strChargeDescription}
                              </div>
                            </td>
                            <td>
                              <div className="flex ">
                                <input
                                  type="text"
                                  name="percentage"
                                  value={value.percentage}
                                  onChange={(event) =>
                                    handleInputChange2(
                                      event,
                                      value.strChargeType
                                    )
                                  }
                                  disabled={!value.isDisabled}
                                  className="block w-44 px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500  transition ease-in-out m-0"
                                />
                                <div className="">
                                  <span className="block p-2 px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500  transition ease-in-out m-0">
                                    %
                                  </span>
                                </div>
                              </div>
                            </td>
                          </tr>
                        ))}
                    </>
                  ) : (
                    <tr>
                      <td colSpan="6" className="px-6 py-1 text-center">
                        <span className="text-sm font-normal text-gray-500">
                          No data available.
                        </span>
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>
          </div>
        </div>

        <div className="bg-gray-100 px-4 py-1 text-right sm:px-6 ">
          <button
            type="button"
            onClick={handleSubmit}
            data-modal-toggle="defaultModal"
            className="inline-flex justify-center rounded-md border border-transparent bg-blue-500 py-1.5 px-3  text-xs font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
          >
            Submit
          </button>
          <button
            type="button"
            onClick={handleClick}
            className="inline-flex justify-center rounded-md border border-transparent bg-rose-500 py-1.5 px-3 mx-4 text-xs font-medium text-white shadow-sm hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
          >
            Clear
          </button>
        </div>
      </div>

      {/* </div> */}
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
